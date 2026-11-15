package com.lichvannien.core.data
import android.content.Context
import com.lichvannien.core.domain.CalendarRepository
import com.lichvannien.core.model.*
import com.lichvannien.core.network.*
import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import java.time.LocalDate
import javax.inject.Singleton
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json

@Module @InstallIn(SingletonComponent::class) object CalendarBindings {
 @Provides @Singleton fun calendar(@ApplicationContext context: Context, clock: Clock): CalendarRepository = DemoCalendarRepository(context,clock)
}
class DemoCalendarRepository(private val context: Context, private val clock: Clock): CalendarRepository {
 override val online = AndroidNetworkMonitor(context).online
 private val days = MutableStateFlow<Map<LocalDate,CachedDay>>(emptyMap())
 private val json = Json { ignoreUnknownKeys = true }
 override fun observeDays(start: LocalDate,endInclusive: LocalDate) = days.map { map -> map.values.filter { it.day.solar in start..endInclusive } }
 override suspend fun refresh(date: LocalDate,force: Boolean): RefreshResult = withContext(Dispatchers.IO) {
  try {
   val text = context.assets.open("calendar/$date.json").bufferedReader().use { it.readText() }
   val value = requireNotNull(json.decodeFromString(DayResponse.serializer(),text).data).validated(date)
   days.update { it + (date to CachedDay(value,clock.instant())) }; RefreshResult.Success
  } catch(e: CancellationException) { throw e }
    catch(_: Exception) { RefreshResult.Failure(DataError.INVALID_DATE) }
 }
}
