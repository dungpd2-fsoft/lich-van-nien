package com.lichvannien.core.domain

import com.lichvannien.core.model.*
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow

interface CalendarRepository {
    val online: Flow<Boolean>
    fun observeDays(start: LocalDate, endInclusive: LocalDate): Flow<List<CachedDay>>
    suspend fun refresh(date: LocalDate, force: Boolean = false): RefreshResult
}
interface SettingsRepository {
    val theme: Flow<ThemeMode>
    suspend fun setTheme(mode: ThemeMode)
}
