package com.lichvannien.core.model

import java.time.Instant
import java.time.LocalDate

enum class Zodiac(val label: String) {
    RAT("Tý"), BUFFALO("Sửu"), TIGER("Dần"), CAT("Mão"), DRAGON("Thìn"), SNAKE("Tỵ"),
    HORSE("Ngọ"), GOAT("Mùi"), MONKEY("Thân"), ROOSTER("Dậu"), DOG("Tuất"), PIG("Hợi");
    companion object { fun fromCanChi(value: String?): Zodiac? = entries.firstOrNull { it.label == value?.trim()?.substringAfterLast(' ') } }
}
enum class DayTheme { HOANG_DAO, QUIET, NEUTRAL }
enum class ThemeMode { SYSTEM, LIGHT, DARK }
data class LunarDate(val day: Int, val month: Int, val year: Int, val leap: Boolean)
data class CanChi(val day: String? = null, val month: String? = null, val year: String? = null)
data class CalendarDay(
    val solar: LocalDate,
    val lunar: LunarDate,
    val canChi: CanChi = CanChi(),
    val solarTerm: String? = null,
    val duty: String? = null,
    val hoangDao: Boolean? = null,
    val blackDay: Boolean? = null,
    val daiCat: Boolean? = null,
    val goodHours: List<String>? = null,
)
data class CachedDay(val day: CalendarDay, val fetchedAt: Instant)
sealed interface RefreshResult {
    data object Success : RefreshResult
    data class Failure(val reason: DataError) : RefreshResult
}
enum class DataError { NETWORK, SERVICE, INVALID_RESPONSE, INVALID_DATE, RATE_LIMITED }
