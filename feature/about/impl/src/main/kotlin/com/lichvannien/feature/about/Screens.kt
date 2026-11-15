package com.lichvannien.feature.about
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable fun AboutRoute(onBack: () -> Unit) { Column(Modifier.padding(24.dp)) { Text("Lịch vạn niên · Nguồn LICH247"); Button(onBack) { Text("Quay lại") } } }
