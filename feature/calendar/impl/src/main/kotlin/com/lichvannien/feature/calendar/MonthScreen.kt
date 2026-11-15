package com.lichvannien.feature.calendar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.lichvannien.core.designsystem.ZodiacImage
import com.lichvannien.core.domain.CalendarRules
import com.lichvannien.core.model.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

val vietnameseDate: DateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy", Locale.forLanguageTag("vi"))

@Composable
fun CalendarRoute(onSettings: () -> Unit, onDetail: (String) -> Unit, model: CalendarViewModel) {
    val state by model.state.collectAsStateWithLifecycle()
    var showPicker by rememberSaveable { mutableStateOf(false) }
    if(showPicker) ChooseDateDialog(state.selected, { showPicker = false }) { model.select(it); showPicker = false }
    MonthScreen(state.month,state.selected,state.days,model::showMonth,model::select,model::today,{showPicker = true},onSettings,{onDetail(state.selected.toString())})
}

@Composable
fun MonthScreen(month: YearMonth, selected: LocalDate, days: Map<LocalDate, CachedDay>, onMonth: (YearMonth) -> Unit, onSelect: (LocalDate) -> Unit, onToday: () -> Unit, onChooseDate: () -> Unit, onSettings: () -> Unit, onDetail: () -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Lịch vạn niên", style = MaterialTheme.typography.headlineSmall)
                TextButton(onSettings) { Text("Cài đặt") }
            }
            Text("LỊCH VIỆT NAM · UTC+7", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { FilledTonalButton(onToday) { Text("Hôm nay") }; FilledTonalButton(onChooseDate) { Text("Chọn ngày") } } }
        item { MonthGrid(month, selected, days, onMonth, onSelect, onChooseDate) }
        item {
            val day = days[selected]?.day
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ZodiacImage(Zodiac.fromCanChi(day?.canChi?.day), Modifier.size(64.dp))
                        Column(Modifier.weight(1f)) {
                            Text(selected.format(vietnameseDate), style = MaterialTheme.typography.titleLarge)
                            Text(day?.lunar?.let { "Âm ${it.day}/${it.month}${if(it.leap) " nhuận" else ""}/${it.year}" } ?: "Chưa có dữ liệu âm lịch")
                            Text(day?.canChi?.day ?: "Chọn xem chi tiết để tra cứu")
                        }
                    }
                    Button(onDetail, Modifier.fillMaxWidth()) { Text("Xem chi tiết") }
                }
            }
        }
        item { Text("Nguồn LICH247 · Thông tin tốt/xấu mang tính tham khảo.", style = MaterialTheme.typography.bodySmall) }
    }
}

@Composable
fun MonthGrid(month: YearMonth, selected: LocalDate, days: Map<LocalDate, CachedDay>, onMonth: (YearMonth) -> Unit, onSelect: (LocalDate) -> Unit, onChooseDate: () -> Unit) {
    Surface(color = MaterialTheme.colorScheme.surfaceContainer, shape = RoundedCornerShape(24.dp)) {
        Column(Modifier.padding(vertical = 12.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                TextButton({ onMonth(month.minusMonths(1)) }) { Text("‹", Modifier.semantics { contentDescription = "Tháng trước" }) }
                Text("Tháng ${month.monthValue}, ${month.year}", Modifier.weight(1f).clickable(onClick = onChooseDate), style = MaterialTheme.typography.titleMedium)
                TextButton({ onMonth(month.plusMonths(1)) }) { Text("›", Modifier.semantics { contentDescription = "Tháng sau" }) }
            }
            Row(Modifier.fillMaxWidth()) { listOf("T2","T3","T4","T5","T6","T7","CN").forEach { Text(it, Modifier.weight(1f).wrapContentWidth(), style = MaterialTheme.typography.labelMedium) } }
            CalendarRules.grid(month).chunked(7).forEach { week ->
                Row(Modifier.fillMaxWidth()) { week.forEach { date ->
                    val chosen = date == selected; val today = date == CalendarRules.today(); val inMonth = YearMonth.from(date) == month; val lunar = days[date]?.day?.lunar
                    Surface(onClick = { onSelect(date) }, modifier = Modifier.weight(1f).heightIn(min = 56.dp).semantics { contentDescription = "${date.format(vietnameseDate)}${if(today) ", hôm nay" else ""}${if(chosen) ", đang chọn" else ""}${lunar?.let { ", âm ${it.day}/${it.month}${if(it.leap) " nhuận" else ""}" } ?: ", chưa có ngày âm"}" }, shape = RoundedCornerShape(16.dp), color = if(chosen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainer, border = if(today && !chosen) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null) {
                        Column(Modifier.padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(date.dayOfMonth.toString(), style = MaterialTheme.typography.titleMedium, color = if(chosen) MaterialTheme.colorScheme.onPrimary else if(inMonth) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(lunar?.let { "${it.day}${if(it.day == 1) "/${it.month}" else ""}${if(it.leap) "n" else ""}" } ?: "—", style = MaterialTheme.typography.labelSmall, color = if(chosen) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                } }
            }
            Text("Số trên: dương · số dưới: âm · n: tháng nhuận", Modifier.padding(12.dp), style = MaterialTheme.typography.labelSmall)
        }
    }
}
