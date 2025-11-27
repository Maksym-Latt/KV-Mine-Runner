package com.chicken.minerunner.presentation.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chicken.minerunner.domain.repository.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val repository: PlayerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState: StateFlow<ProgressUiState> = _uiState.asStateFlow()

    init {
        repository.state.onEach { state ->
            _uiState.updateWith(state)
        }.launchIn(viewModelScope)
    }

    fun addEggs(amount: Int) {
        viewModelScope.launch { repository.addEggs(amount) }
    }

    fun nextItem() {
        val nextIndex = (_uiState.value.currentItemIndex + 1).coerceAtMost(_uiState.value.shopItems.lastIndex)
        _uiState.value = _uiState.value.copy(currentItemIndex = nextIndex)
    }

    fun previousItem() {
        val nextIndex = (_uiState.value.currentItemIndex - 1).coerceAtLeast(0)
        _uiState.value = _uiState.value.copy(currentItemIndex = nextIndex)
    }

    fun purchaseOrUpgrade() {
        val item = _uiState.value.shopItems.getOrNull(_uiState.value.currentItemIndex) ?: return
        val price = item.nextPrice ?: return
        viewModelScope.launch {
            val success = repository.upgradeItem(item.id, price, item.maxLevel)
            _uiState.value = if (success) {
                _uiState.value.copy(message = null)
            } else {
                _uiState.value.copy(message = "Not enough eggs")
            }
        }
    }

    fun dismissMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }

    fun toggleSettings(open: Boolean) {
        _uiState.value = _uiState.value.copy(settingsVisible = open)
    }

    fun setMusicEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.setMusicEnabled(enabled) }
    }

    fun setSfxEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.setSfxEnabled(enabled) }
    }
}

private fun MutableStateFlow<ProgressUiState>.updateWith(state: PlayerState) {
    val adjustedIndex = state.shopItems.ifEmpty { emptyList() }.let { items ->
        value.currentItemIndex.coerceIn(0, (items.size - 1).coerceAtLeast(0))
    }
    value = value.copy(
        eggs = state.eggs,
        shopItems = state.shopItems,
        currentItemIndex = adjustedIndex,
        musicEnabled = state.musicEnabled,
        sfxEnabled = state.sfxEnabled
    )
}
