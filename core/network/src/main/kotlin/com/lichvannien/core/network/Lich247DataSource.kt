package com.lichvannien.core.network

import com.lichvannien.core.model.*
import java.io.IOException
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

@Serializable data class DateParts(val day: Int? = null, val month: Int? = null, val year: Int? = null, val leap: Int? = null)
@Serializable data class ChiDto(val day: String? = null, val month: String? = null, val year: String? = null)
@Serializable data class DayDto(val solar: DateParts? = null, val lunar: DateParts? = null, val can_chi: ChiDto? = null, val tiet_khi: String? = null, val truc: String? = null, val isHoangDao: Boolean? = null, val isBlackDay: Boolean? = null, val isDaiCat: Boolean? = null, val gioHoangDao: List<String>? = null)
@Serializable data class DayResponse(val status: String? = null, val data: DayDto? = null)
interface Lich247Api { @GET("api/v1/calendar") suspend fun day(@Query("date") date: String): Response<DayResponse> }
sealed interface RemoteResult {
    data class Success(val day: CalendarDay) : RemoteResult
    data class Failure(val error: DataError, val retryAfterSeconds: Long? = null) : RemoteResult
}

class Lich247DataSource {
    private val json = Json { ignoreUnknownKeys = true }
    private val client = OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).callTimeout(30, TimeUnit.SECONDS).build()
    private val api = Retrofit.Builder().baseUrl("https://lich247.com/").client(client).addConverterFactory(json.asConverterFactory("application/json".toMediaType())).build().create(Lich247Api::class.java)
    suspend fun load(date: LocalDate): RemoteResult = try {
        val response = api.day(date.toString())
        when {
            response.code() == 429 -> RemoteResult.Failure(DataError.RATE_LIMITED, retryAfter(response.headers()["Retry-After"]))
            response.code() == 400 -> RemoteResult.Failure(DataError.INVALID_DATE)
            !response.isSuccessful -> RemoteResult.Failure(DataError.SERVICE)
            else -> {
                val body = response.body()
                if(body?.status != "success") RemoteResult.Failure(DataError.INVALID_RESPONSE)
                else RemoteResult.Success(requireNotNull(body.data).validated(date))
            }
        }
    } catch(e: CancellationException) { throw e }
      catch(_: IOException) { RemoteResult.Failure(DataError.NETWORK) }
      catch(_: SerializationException) { RemoteResult.Failure(DataError.INVALID_RESPONSE) }
      catch(_: IllegalArgumentException) { RemoteResult.Failure(DataError.INVALID_RESPONSE) }
      catch(_: java.time.DateTimeException) { RemoteResult.Failure(DataError.INVALID_RESPONSE) }
}

fun DayDto.validated(requested: LocalDate): CalendarDay {
    val s = requireNotNull(solar)
    val actual = LocalDate.of(requireNotNull(s.year),requireNotNull(s.month),requireNotNull(s.day))
    require(actual == requested) { "Response date does not match request" }
    val l = requireNotNull(lunar)
    val day = requireNotNull(l.day); val month = requireNotNull(l.month); val year = requireNotNull(l.year)
    require(day in 1..30 && month in 1..12 && year in 1..9999 && l.leap in listOf(0,1))
    return CalendarDay(actual,LunarDate(day,month,year,l.leap == 1),CanChi(can_chi?.day,can_chi?.month,can_chi?.year),tiet_khi,truc,isHoangDao,isBlackDay,isDaiCat,gioHoangDao)
}
private fun retryAfter(value: String?): Long? = value?.toLongOrNull()?.coerceIn(0,3600) ?: runCatching {
    val time = java.time.ZonedDateTime.parse(value,java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME).toInstant()
    java.time.Duration.between(java.time.Instant.now(),time).seconds.coerceIn(0,3600)
}.getOrNull()
