package com.lichvannien.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.lichvannien.core.model.ThemeMode

fun ThemeMode.label(): String = when(this) { ThemeMode.SYSTEM -> "Theo hệ thống"; ThemeMode.LIGHT -> "Sáng"; ThemeMode.DARK -> "Tối" }

@Composable
fun SettingsRoute(onBack: () -> Unit, onAbout: () -> Unit) {
    var mode by rememberSaveable { mutableStateOf(ThemeMode.SYSTEM) }
    SettingsScreen(mode, { mode = it }, onBack, onAbout)
}

@Composable
fun SettingsScreen(mode: ThemeMode, onModeChange: (ThemeMode) -> Unit, onBack: () -> Unit, onAbout: () -> Unit) {
    var showTheme by rememberSaveable { mutableStateOf(false) }
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        item { TextButton(onBack) { Text("‹ Về lịch tháng") }; Text("Cài đặt", style = MaterialTheme.typography.headlineLarge) }
        item { Text("Cá nhân hóa", color = MaterialTheme.colorScheme.primary) }
        item { SettingsItem("Giao diện", mode.label(), { showTheme = true }) }
        item { SettingsItem("Lịch Việt Nam", "Ngày hôm nay theo múi giờ UTC+7", null) }
        item { SettingsItem("Thông tin ứng dụng & nguồn", "Phiên bản, dữ liệu và quyền riêng tư", onAbout) }
        item { Text("Chuyển động tuân theo thiết lập hệ thống.\nDữ liệu ngày đã tải có thể xem lại khi offline.", style = MaterialTheme.typography.bodyMedium) }
    }
    if(showTheme) ThemeDialog(mode, { showTheme = false }) { value -> showTheme = false; if(value != mode) onModeChange(value) }
}

@Composable
private fun SettingsItem(title: String, detail: String, onClick: (() -> Unit)?) {
    Card(Modifier.fillMaxWidth().then(if(onClick != null) Modifier.clickable(onClick = onClick) else Modifier)) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Text(title, style = MaterialTheme.typography.titleMedium); Text(detail, style = MaterialTheme.typography.bodyMedium) }
    }
}

@Composable
fun ThemeDialog(initial: ThemeMode, onDismiss: () -> Unit, onApply: (ThemeMode) -> Unit) {
    var draft by rememberSaveable { mutableStateOf(initial) }
    AlertDialog(onDismissRequest = onDismiss, title = { Text("Chọn giao diện") }, text = {
        Column(Modifier.selectableGroup()) { ThemeMode.entries.forEach { mode ->
            Row(Modifier.fillMaxWidth().heightIn(min = 56.dp).selectable(draft == mode, role = Role.RadioButton, onClick = { draft = mode }), verticalAlignment = Alignment.CenterVertically) {
                RadioButton(draft == mode, onClick = null); Spacer(Modifier.width(12.dp)); Text(mode.label())
            }
        } }
    }, confirmButton = { TextButton({ onApply(draft) }) { Text("Áp dụng") } }, dismissButton = { TextButton(onDismiss) { Text("Hủy") } })
}
