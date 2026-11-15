package com.lichvannien.feature.calendar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.ui.input.pointer.pointerInput
import com.lichvannien.core.designsystem.MonthBanner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.lichvannien.core.ui.FeedbackCard
import com.lichvannien.core.domain.CalendarRules
import com.lichvannien.core.model.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

val vietnameseDate: DateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy", Locale.forLanguageTag("vi"))

@Composable
fun MonthScreen(month: YearMonth, selected: LocalDate, days: Map<LocalDate, CachedDay>, onMonth: (YearMonth) -> Unit, onSelect: (LocalDate) -> Unit, onToday: () -> Unit, onChooseDate: () -> Unit, onSettings: () -> Unit, onDetail: (() -> Unit)?, modifier: Modifier = Modifier, listState: LazyListState = rememberLazyListState(), feedback: Pair<String,String>? = null, onRetry: (() -> Unit)? = null) {
    LazyColumn(modifier.fillMaxSize(), state = listState, contentPadding = PaddingValues(horizontal = 12.dp, vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Lịch vạn niên", Modifier.weight(1f), style = MaterialTheme.typography.headlineSmall)
                TextButton(onSettings) { Text("Cài đặt") }
            }
            Text("LỊCH VIỆT NAM · UTC+7", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item { MonthBanner(month.monthValue,month.year) }
        item { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { FilledTonalButton(onToday, Modifier.weight(1f)) { Text("Hôm nay") }; Spacer(Modifier.width(8.dp)); FilledTonalButton(onChooseDate, Modifier.weight(1f)) { Text("Chọn ngày") } } }
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
                    if(onDetail != null) Button(onDetail, Modifier.fillMaxWidth()) { Text("Xem chi tiết") }
                }
            }
        }
        if(feedback != null) item { FeedbackCard(feedback.first,feedback.second,onRetry) }
        item { Text("Nguồn LICH247 · Thông tin tốt/xấu mang tính tham khảo.", style = MaterialTheme.typography.bodySmall) }
    }
}

@Composable
private fun MonthGridContent(month: YearMonth, selected: LocalDate, days: Map<LocalDate, CachedDay>, onMonth: (YearMonth) -> Unit, onSelect: (LocalDate) -> Unit, onChooseDate: () -> Unit) {
    Surface(color = MaterialTheme.colorScheme.surfaceContainer, shape = RoundedCornerShape(24.dp)) {
        BoxWithConstraints(Modifier.fillMaxWidth()) {
        val gridWidth = maxOf(maxWidth,336.dp)
        Row(Modifier.horizontalScroll(rememberScrollState())) {
        Column(Modifier.width(gridWidth).padding(vertical = 12.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                TextButton({ onMonth(month.minusMonths(1)) }, enabled = month > YearMonth.of(1,1)) { Text("‹", Modifier.semantics { contentDescription = "Tháng trước" }) }
                Text("Tháng ${month.monthValue}, ${month.year}", Modifier.weight(1f).clickable(onClick = onChooseDate), style = MaterialTheme.typography.titleMedium)
                TextButton({ onMonth(month.plusMonths(1)) }, enabled = month < YearMonth.of(9999,12)) { Text("›", Modifier.semantics { contentDescription = "Tháng sau" }) }
            }
            Row(Modifier.fillMaxWidth()) { listOf("T2","T3","T4","T5","T6","T7","CN").forEach { Text(it, Modifier.weight(1f).wrapContentWidth(), style = MaterialTheme.typography.labelMedium) } }
            CalendarRules.grid(month).chunked(7).forEach { week ->
                Row(Modifier.fillMaxWidth()) { week.forEach { date ->
                    val chosen = date == selected;
                    val animatedColor by animateColorAsState(if(chosen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainer, label = "selectedDay"); val today = date == CalendarRules.today(); val inMonth = YearMonth.from(date) == month; val lunar = days[date]?.day?.lunar
                    Surface(onClick = { onSelect(date) }, enabled = date.year in 1..9999, modifier = Modifier.weight(1f).heightIn(min = 56.dp).semantics { contentDescription = "${date.format(vietnameseDate)}${if(today) ", hôm nay" else ""}${if(chosen) ", đang chọn" else ""}${lunar?.let { ", âm ${it.day}/${it.month}${if(it.leap) " nhuận" else ""}" } ?: ", chưa có ngày âm"}" }, shape = RoundedCornerShape(16.dp), color = animatedColor, border = if(today && !chosen) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null) {
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
    }
}

@Composable
fun MonthGrid(month: YearMonth, selected: LocalDate, days: Map<LocalDate,CachedDay>, onMonth: (YearMonth) -> Unit, onSelect: (LocalDate) -> Unit, onChooseDate: () -> Unit) {
    Box(Modifier.pointerInput(month) {
        var distance = 0f
        detectHorizontalDragGestures(onDragStart = { distance = 0f }, onDragEnd = {
            if(distance > 80 * density && month > YearMonth.of(1,1)) onMonth(month.minusMonths(1))
            else if(distance < -80 * density && month < YearMonth.of(9999,12)) onMonth(month.plusMonths(1))
        }, onHorizontalDrag = { change, amount -> change.consume(); distance += amount })
    }) {
        AnimatedContent(month, transitionSpec = {
            if(targetState > initialState) (slideInHorizontally(tween(200)) { it / 5 } + fadeIn()) togetherWith (slideOutHorizontally(tween(180)) { -it / 5 } + fadeOut())
            else (slideInHorizontally(tween(200)) { -it / 5 } + fadeIn()) togetherWith (slideOutHorizontally(tween(180)) { it / 5 } + fadeOut())
        }, label = "monthTransition") { shown -> MonthGridContent(shown,selected,days,onMonth,onSelect,onChooseDate) }
    }
}
