package com.lichvannien.feature.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.LocalDate
import java.time.Duration
import java.time.Instant

@Composable
fun CalendarRoute(onSettings: () -> Unit, onDetail: (String) -> Unit, model: CalendarViewModel) {
    CalendarAdaptiveRoute(model,onSettings,onDetail,false,null)
}

@Composable
fun DayRoute(date: String, onBack: () -> Unit, model: CalendarViewModel, onSettings: () -> Unit = {}) {
    var initialized by rememberSaveable(date) { mutableStateOf(false) }
    LaunchedEffect(date) { if(!initialized) { model.select(LocalDate.parse(date)); initialized = true } }
    CalendarAdaptiveRoute(model,onSettings,{},true,onBack)
}

@Composable
private fun CalendarAdaptiveRoute(model: CalendarViewModel, onSettings: () -> Unit, onDetail: (String) -> Unit, detailPage: Boolean, onBack: (() -> Unit)?) {
    val state by model.state.collectAsStateWithLifecycle()
    var showPicker by rememberSaveable { mutableStateOf(false) }
    val monthScroll = rememberLazyListState()
    val dayScroll = rememberLazyListState()
    val selected = state.selected
    val cached = state.days[selected]
    val status = state.refresh[selected]
    val stale = cached != null && (!state.online || status?.error != null || Duration.between(cached.fetchedAt,Instant.now()).seconds >= 300)
    val feedback = when {
        stale -> "Bạn đang xem dữ liệu đã lưu" to "Dữ liệu lưu trên thiết bị; thử tải lại khi có kết nối."
        cached == null && status?.error != null -> "Chưa tải được thông tin ngày" to if(status.error == com.lichvannien.core.model.DataError.INVALID_DATE) "Nguồn chưa có dữ liệu cho ngày này." else "Kiểm tra kết nối hoặc thử lại sau."
        cached == null && status?.loading == true -> "Đang tải thông tin ngày…" to "Bạn vẫn có thể chuyển tháng và chọn ngày."
        else -> null
    }
    if(showPicker) ChooseDateDialog(selected, { showPicker = false }) { model.select(it); showPicker = false }
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val wide = maxWidth >= 840.dp
        if(wide) {
            Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                MonthScreen(state.month,selected,state.days,model::showMonth,model::select,model::today,{showPicker = true},onSettings,null,Modifier.weight(1f),monthScroll,feedback,if(status?.loading == true) null else { {model.retry()} })
                DayScreen(selected,cached,status?.loading == true,status?.error,onBack,{model.retry()},Modifier.weight(1f),stale,dayScroll)
            }
        } else {
            Box(Modifier.fillMaxSize(),contentAlignment = Alignment.TopCenter) {
                val width = Modifier.widthIn(max = 640.dp).fillMaxSize()
                if(detailPage) DayScreen(selected,cached,status?.loading == true,status?.error,onBack,{model.retry()},width,stale,dayScroll)
                else MonthScreen(state.month,selected,state.days,model::showMonth,model::select,model::today,{showPicker = true},onSettings,{onDetail(selected.toString())},width,monthScroll,feedback,if(status?.loading == true) null else { {model.retry()} })
            }
        }
    }
}
