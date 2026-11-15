package com.lichvannien.feature.calendar
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable fun DayRoute(date: String, onBack: () -> Unit) { Column(Modifier.padding(24.dp)) { Text("Chi tiết ngày $date"); Button(onBack) { Text("Về lịch tháng") } } }
