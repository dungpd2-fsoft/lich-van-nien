package com.lichvannien.feature.calendar.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object CalendarKey : NavKey
@Serializable data class DayKey(val date: String) : NavKey
