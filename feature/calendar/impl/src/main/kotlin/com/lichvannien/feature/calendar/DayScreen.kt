package com.lichvannien.feature.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lichvannien.core.designsystem.*
import com.lichvannien.core.domain.CalendarRules
import com.lichvannien.core.model.*
import com.lichvannien.core.ui.FeedbackCard
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DayScreen(date: LocalDate, cached: CachedDay?, loading: Boolean, error: DataError?, onBack: (() -> Unit)? = null, onRetry: () -> Unit, modifier: Modifier = Modifier, stale: Boolean = false, listState: LazyListState = rememberLazyListState()) {
    val day = cached?.day
    LazyColumn(modifier.fillMaxSize(), state = listState, contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            if(onBack != null) TextButton(onBack) { Text("‹ Về lịch tháng") }
            Text(date.format(vietnameseDate), style = MaterialTheme.typography.headlineSmall)
        }
        item { DayHero(day, CalendarRules.dayTheme(day)) }
        if(loading) item { LinearProgressIndicator(Modifier.fillMaxWidth()); Text("Đang tải thông tin ngày…") }
        if(stale && cached != null) item { FeedbackCard("Bạn đang xem dữ liệu đã lưu", "Lưu lúc " + DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy").withZone(CalendarRules.vietnamZone).format(cached.fetchedAt), if(loading) null else onRetry) }
        if(error != null && cached == null) item { FeedbackCard(if(error == DataError.INVALID_RESPONSE) "Chưa nhận được dữ liệu hợp lệ cho ngày này" else "Chưa tải được thông tin ngày", when(error) { DataError.NETWORK -> "Kiểm tra kết nối rồi thử lại."; DataError.RATE_LIMITED -> "Dịch vụ đang giới hạn truy cập. Vui lòng thử lại sau."; DataError.INVALID_DATE -> "Nguồn chưa hỗ trợ ngày này."; else -> "Dịch vụ tạm thời chưa phản hồi. Vui lòng thử lại." }, if(loading) null else onRetry) }
        if(day == null && !loading && error == null) item { FeedbackCard("Chưa có dữ liệu âm lịch", "Ngày dương vẫn có thể tra cứu khi dữ liệu sẵn sàng.", onRetry) }
        if(day != null) {
            item {
                Text("Can chi hôm nay", style = MaterialTheme.typography.titleLarge)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    listOf("Ngày" to day.canChi.day, "Tháng" to day.canChi.month, "Năm" to day.canChi.year).forEach { (title,value) ->
                        Card { Column(Modifier.padding(16.dp).widthIn(min = 64.dp), horizontalAlignment = Alignment.CenterHorizontally) { ZodiacImage(Zodiac.fromCanChi(value), Modifier.size(64.dp)); Text(title, style = MaterialTheme.typography.labelMedium); Text(value ?: "Chưa có thông tin") } }
                    }
                }
            }
            item { InfoRows("Thông tin ngày", listOf("Can chi tháng" to day.canChi.month, "Tiết khí" to day.solarTerm, "Trực" to day.duty)) }
            item { InfoRows("Phân loại theo nguồn", listOf("Hoàng đạo" to day.hoangDao.readable(), "Hắc đạo" to day.blackDay.readable(), "Đại cát" to day.daiCat.readable())) }
            item {
                Text("Giờ hoàng đạo", style = MaterialTheme.typography.titleLarge)
                val hours = day.goodHours
                if(hours == null) Text("Chưa có thông tin")
                else if(hours.isEmpty()) Text("Nguồn không trả giờ hoàng đạo")
                else FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { hours.forEach { hour -> SuggestionChip(onClick = {}, label = { Text(hour) }) } }
            }
        }
        item { Text("Nguồn LICH247 · Lịch Việt Nam UTC+7\nTranh minh họa. Thông tin tốt/xấu chỉ để tham khảo.", style = MaterialTheme.typography.bodySmall) }
    }
}

@Composable
private fun InfoRows(title: String, rows: List<Pair<String,String?>>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge)
        Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { rows.forEach { (label,value) -> FlowRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) { Text(label, style = MaterialTheme.typography.labelLarge); Text(value ?: "Chưa có thông tin") } } } }
    }
}
private fun Boolean?.readable(): String = when(this) { true -> "Có"; false -> "Không"; null -> "Chưa có thông tin" }
