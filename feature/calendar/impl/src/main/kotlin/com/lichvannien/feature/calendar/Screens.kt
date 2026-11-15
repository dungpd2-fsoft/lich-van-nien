package com.lichvannien.feature.calendar
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable fun CalendarRoute(onSettings: () -> Unit, onDetail: (String) -> Unit) { Column(Modifier.padding(24.dp)) { Text("Lịch tháng"); Button(onSettings) { Text("Cài đặt") }; Button({ onDetail("2026-11-15") }) { Text("Xem chi tiết") } } }
@Composable fun DayRoute(date: String, onBack: () -> Unit) { Column(Modifier.padding(24.dp)) { Text("Chi tiết ngày $date"); Button(onBack) { Text("Về lịch tháng") } } }
