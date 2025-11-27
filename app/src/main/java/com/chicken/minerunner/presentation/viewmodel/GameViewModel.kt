package com.chicken.minerunner.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class GameState { Idle, Running, Paused, GameOver }

data class GameStats(
    val eggs: Int = 1500,
    val coins: Int = 1500,
    val distance: Int = 128,
    val lives: Int = 3
)

data class GameUiState(
    val state: GameState = GameState.Idle,
    val stats: GameStats = GameStats()
)

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    fun startRun() {
        _uiState.value = _uiState.value.copy(state = GameState.Running)
    }

    fun pause() {
        _uiState.value = _uiState.value.copy(state = GameState.Paused)
    }

    fun finish() {
        _uiState.value = _uiState.value.copy(state = GameState.GameOver)
    }

    fun resetRun() {
        _uiState.value = GameUiState(state = GameState.Running, stats = GameStats())
    }

    fun collectEgg() {
        updateStats { it.copy(eggs = it.eggs + 1) }
    }

    fun takeHit() {
        updateStats { stats ->
            val remaining = (stats.lives - 1).coerceAtLeast(0)
            stats.copy(lives = remaining)
        }
    }

    fun advanceDistance() {
        updateStats { it.copy(distance = it.distance + 5) }
    }

    private fun updateStats(block: (GameStats) -> GameStats) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(stats = block(_uiState.value.stats))
        }
    }
}
