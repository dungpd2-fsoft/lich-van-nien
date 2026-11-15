package com.lichvannien.core.domain

import com.lichvannien.core.model.*
import java.time.Clock
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

object CalendarRules {
    val vietnamZone: ZoneId = ZoneId.of("Asia/Ho_Chi_Minh")
    fun today(clock: Clock = Clock.systemUTC()): LocalDate = LocalDate.now(clock.withZone(vietnamZone))
    fun grid(month: YearMonth): List<LocalDate> {
        val first = month.atDay(1)
        val start = first.minusDays((first.dayOfWeek.value - 1).toLong())
        return List(42) { start.plusDays(it.toLong()) }
    }
    fun parse(day: String, month: String, year: String): LocalDate? = runCatching {
        require(year.length == 4)
        LocalDate.of(year.toInt(), month.toInt(), day.toInt())
    }.getOrNull()
    fun dayTheme(day: CalendarDay?): DayTheme = when {
        day?.hoangDao == true && day.blackDay == false -> DayTheme.HOANG_DAO
        day?.blackDay == true && day.hoangDao == false -> DayTheme.QUIET
        else -> DayTheme.NEUTRAL
    }
}
