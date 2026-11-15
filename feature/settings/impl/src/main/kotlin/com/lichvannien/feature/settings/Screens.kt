package com.lichvannien.feature.settings
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable fun SettingsRoute(onBack: () -> Unit, onAbout: () -> Unit) { Column(Modifier.padding(24.dp)) { Text("Cài đặt"); Button(onAbout) { Text("Thông tin ứng dụng") }; Button(onBack) { Text("Về lịch") } } }
