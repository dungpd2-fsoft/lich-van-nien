package com.lichvannien.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lichvannien.core.domain.SettingsRepository
import com.lichvannien.core.model.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

data class UiMessage(val id: Long, val text: String)
@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: SettingsRepository): ViewModel() {
    val theme = repository.theme.stateIn(viewModelScope,SharingStarted.Eagerly,ThemeMode.SYSTEM)
    private val _messages = MutableStateFlow<List<UiMessage>>(emptyList())
    val messages = _messages.asStateFlow()
    private val _saving = MutableStateFlow(false)
    val saving = _saving.asStateFlow()
    private var nextId = 0L
    fun save(mode: ThemeMode) {
        if(mode == theme.value || !_saving.compareAndSet(false,true)) return
        viewModelScope.launch {
            try { repository.setTheme(mode); message("Đã cập nhật giao diện") }
            catch(e: CancellationException) { throw e }
            catch(_: Exception) { message("Không lưu được giao diện. Vui lòng thử lại.") }
            finally { _saving.value = false }
        }
    }
    fun handled(id: Long) { _messages.update { list -> list.filterNot { it.id == id } } }
    private fun message(text: String) { val item = UiMessage(++nextId,text); _messages.update { it + item } }
}
