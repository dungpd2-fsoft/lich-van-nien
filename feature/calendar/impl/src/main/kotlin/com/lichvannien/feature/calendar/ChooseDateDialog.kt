package com.lichvannien.feature.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.lichvannien.core.domain.CalendarRules
import java.time.LocalDate

@Composable
fun ChooseDateDialog(initial: LocalDate, onDismiss: () -> Unit, onConfirm: (LocalDate) -> Unit) {
    var day by rememberSaveable { mutableStateOf(initial.dayOfMonth.toString()) }
    var month by rememberSaveable { mutableStateOf(initial.monthValue.toString()) }
    var year by rememberSaveable { mutableStateOf(initial.year.toString()) }
    var error by rememberSaveable { mutableStateOf(false) }
    val dayFocus = remember { FocusRequester() }
    val monthFocus = remember { FocusRequester() }
    val yearFocus = remember { FocusRequester() }
    LaunchedEffect(error) { if(error) {
        when { year.length != 4 || year.toIntOrNull() !in 1..9999 -> yearFocus; month.toIntOrNull() !in 1..12 -> monthFocus; else -> dayFocus }.requestFocus()
    } }
    AlertDialog(onDismissRequest = onDismiss, title = { Text("Chọn ngày dương lịch") }, text = {
        Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Nhập ngày cần tra cứu. Lịch Việt Nam theo UTC+7.")
            OutlinedTextField(day, { day = it.filter(Char::isDigit).take(2); error = false }, modifier = Modifier.focusRequester(dayFocus), label = { Text("Ngày") }, isError = error, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
            OutlinedTextField(month, { month = it.filter(Char::isDigit).take(2); error = false }, modifier = Modifier.focusRequester(monthFocus), label = { Text("Tháng") }, isError = error, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
            OutlinedTextField(year, { year = it.filter(Char::isDigit).take(4); error = false }, modifier = Modifier.focusRequester(yearFocus), label = { Text("Năm") }, isError = error, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
            if(error) Text("Ngày không hợp lệ. Vui lòng kiểm tra ngày, tháng, năm.", color = MaterialTheme.colorScheme.error)
        }
    }, confirmButton = { TextButton({ val date = CalendarRules.parse(day,month,year); if(date == null || date.year !in 1..9999) error = true else onConfirm(date) }) { Text("Xem ngày") } }, dismissButton = { TextButton(onDismiss) { Text("Hủy") } })
}
