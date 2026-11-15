package com.lichvannien.feature.calendar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lichvannien.core.domain.*
import com.lichvannien.core.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.*
import javax.inject.Inject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

data class RefreshState(val loading: Boolean = false, val error: DataError? = null)
data class CalendarUiState(val month: YearMonth, val selected: LocalDate, val days: Map<LocalDate,CachedDay> = emptyMap(), val refresh: Map<LocalDate,RefreshState> = emptyMap())

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CalendarViewModel @Inject constructor(private val repository: CalendarRepository, private val clock: Clock, private val saved: SavedStateHandle) : ViewModel() {
    private val initial = CalendarRules.today(clock)
    private val selected = saved.getStateFlow("selected", initial.toString())
    private val month = saved.getStateFlow("month", YearMonth.from(initial).toString())
    private val refresh = MutableStateFlow<Map<LocalDate,RefreshState>>(emptyMap())
    private val selection = combine(month, selected) { m,d -> YearMonth.parse(m) to LocalDate.parse(d) }
    val state: StateFlow<CalendarUiState> = selection.flatMapLatest { (m,d) ->
        val grid = CalendarRules.grid(m)
        combine(repository.observeDays(grid.first(),grid.last()),repository.observeDays(d,d),refresh) { rows, selectedRows, status ->
            CalendarUiState(m,d,(rows + selectedRows).associateBy { it.day.solar },status)
        }
    }.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),CalendarUiState(YearMonth.parse(month.value),LocalDate.parse(selected.value)))
    init {
        viewModelScope.launch {
            selection.collectLatest { (m,d) ->
                refresh.value = emptyMap()
                supervisorScope {
                    val dates = (listOf(d) + CalendarRules.grid(m).filter { YearMonth.from(it) == m }).distinct()
                    dates.forEach { date -> launch { load(date,false) } }
                }
            }
        }
    }
    fun select(date: LocalDate) { require(date.year in 1..9999); saved["selected"] = date.toString(); saved["month"] = YearMonth.from(date).toString() }
    fun showMonth(value: YearMonth) { if(value.year in 1..9999) saved["month"] = value.toString() }
    fun today() = select(CalendarRules.today(clock))
    fun retry(date: LocalDate = LocalDate.parse(selected.value)) { viewModelScope.launch { load(date,true) } }
    fun refreshCurrent() { viewModelScope.launch { load(LocalDate.parse(selected.value),false) } }
    private suspend fun load(date: LocalDate, force: Boolean) {
        refresh.update { it + (date to RefreshState(loading = true)) }
        val result = try { repository.refresh(date,force) } catch(e: CancellationException) { throw e } catch(_: Exception) { RefreshResult.Failure(DataError.SERVICE) }
        refresh.update { it + (date to RefreshState(error = (result as? RefreshResult.Failure)?.reason)) }
    }
}
