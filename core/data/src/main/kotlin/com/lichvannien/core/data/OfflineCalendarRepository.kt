package com.lichvannien.core.data

import com.lichvannien.core.database.*
import com.lichvannien.core.domain.CalendarRepository
import com.lichvannien.core.model.*
import com.lichvannien.core.network.*
import java.time.Clock
import java.time.Duration
import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

class OfflineCalendarRepository(private val dao: CalendarDao, private val remote: Lich247DataSource, private val clock: Clock, monitor: AndroidNetworkMonitor) : CalendarRepository {
 override val online = monitor.online
 private val permits = Semaphore(2)
 private val pending = ConcurrentHashMap<LocalDate,CompletableDeferred<RefreshResult>>()
 private val retryAt = AtomicLong(0)
 override fun observeDays(start: LocalDate, endInclusive: LocalDate): Flow<List<CachedDay>> = dao.observe(start.toString(),endInclusive.toString()).map { rows -> rows.map { it.toModel() } }.flowOn(Dispatchers.IO)
 override suspend fun refresh(date: LocalDate, force: Boolean): RefreshResult = withContext(Dispatchers.IO) {
  val signal = CompletableDeferred<RefreshResult>()
  val existing = pending.putIfAbsent(date,signal)
  if(existing != null) return@withContext existing.await()
  try {
   val result = permits.withPermit {
    val cache = dao.get(date.toString())
    val age = cache?.let { Duration.between(java.time.Instant.ofEpochMilli(it.fetchedAt),clock.instant()).seconds }
    if(!force && age != null && age in 0..299) return@withPermit RefreshResult.Success
    if(clock.millis() < retryAt.get()) return@withPermit RefreshResult.Failure(DataError.RATE_LIMITED)
    var response = remote.load(date)
    if(response is RemoteResult.Failure && response.error in listOf(DataError.NETWORK,DataError.SERVICE)) { delay(750); response = remote.load(date) }
    when(val value = response) {
     is RemoteResult.Success -> { dao.upsert(CachedDay(value.day,clock.instant()).toEntity()); RefreshResult.Success }
     is RemoteResult.Failure -> {
      if(value.error == DataError.RATE_LIMITED) retryAt.updateAndGet { previous -> maxOf(previous,clock.millis() + (value.retryAfterSeconds ?: 60) * 1000) }
      RefreshResult.Failure(value.error)
     }
    }
   }
   signal.complete(result); result
  } catch(e: CancellationException) { signal.cancel(e); throw e }
    catch(e: Exception) { signal.completeExceptionally(e); throw e }
    finally { pending.remove(date,signal) }
 }
}
