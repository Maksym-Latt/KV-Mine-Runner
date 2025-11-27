package com.chicken.minerunner.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chicken.minerunner.core.domain.model.GameSnapshot
import com.chicken.minerunner.core.domain.model.ItemType
import com.chicken.minerunner.core.domain.model.LanePosition
import com.chicken.minerunner.core.domain.model.SwipeDirection
import com.chicken.minerunner.core.domain.model.TrackType
import com.chicken.minerunner.core.domain.repository.GameRepository
import com.chicken.minerunner.core.domain.usecase.GenerateTrackRowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val generateTrackRowUseCase: GenerateTrackRowUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameSnapshot())
    val uiState: StateFlow<GameSnapshot> = _uiState.asStateFlow()

    init {
        reset()
    }

    fun reset() {
        val initialRows = buildList {
            add(generateTrackRowUseCase.invoke(0, isSafeZone = true))
            add(generateTrackRowUseCase.invoke(1, isSafeZone = true))
            for (i in 2..8) {
                add(generateTrackRowUseCase.invoke(i, isSafeZone = false))
            }
        }
        _uiState.value = GameSnapshot(rows = initialRows)
    }

    fun pause() {
        _uiState.value = _uiState.value.copy(isPaused = true)
    }

    fun resume() {
        if (!_uiState.value.gameOver) {
            _uiState.value = _uiState.value.copy(isPaused = false)
        }
    }

    fun swipe(direction: SwipeDirection) {
        val state = _uiState.value
        if (state.isPaused || state.gameOver) return
        when (direction) {
            SwipeDirection.LEFT -> moveHorizontal(-1)
            SwipeDirection.RIGHT -> moveHorizontal(1)
            SwipeDirection.FORWARD -> stepForward()
        }
    }

    private fun moveHorizontal(delta: Int) {
        val order = listOf(LanePosition.LEFT, LanePosition.CENTER, LanePosition.RIGHT)
        val index = order.indexOf(_uiState.value.lane)
        val newIndex = (index + delta).coerceIn(0, order.lastIndex)
        _uiState.value = _uiState.value.copy(lane = order[newIndex])
    }

    private fun stepForward() {
        val current = _uiState.value
        val nextIndex = current.currentRowIndex + 1
        ensureRows(nextIndex)
        val nextRow = _uiState.value.rows[nextIndex]
        val laneContent = nextRow.lanes.first { it.lane == current.lane }

        var lives = current.lives
        var eggs = current.eggs
        var helmetActive = current.helmetActive
        var magnetActive = current.magnetActive

        if (nextRow.type == TrackType.RAILWAY && laneContent.hasTrolley) {
            if (helmetActive) {
                helmetActive = false
            } else {
                lives -= 1
            }
        }

        when (laneContent.item) {
            ItemType.EGG -> eggs += if (magnetActive) 2 else 1
            ItemType.EXTRA_LIFE -> lives += 1
            ItemType.HELMET -> helmetActive = true
            ItemType.MAGNET -> magnetActive = true
            null -> {}
        }

        val updated = current.copy(
            currentRowIndex = nextIndex,
            distance = current.distance + 1,
            eggs = eggs,
            lives = lives,
            helmetActive = helmetActive,
            magnetActive = magnetActive,
            gameOver = lives <= 0
        )
        _uiState.value = updated
        persistProgress(updated)
    }

    private fun ensureRows(index: Int) {
        val currentRows = _uiState.value.rows.toMutableList()
        if (index >= currentRows.lastIndex - 2) {
            val nextId = currentRows.maxOf { it.id } + 1
            val isSafe = nextId % 7 == 0 || (nextId - 1) % 7 == 0
            currentRows.add(generateTrackRowUseCase.invoke(nextId, isSafe))
        }
        _uiState.value = _uiState.value.copy(rows = currentRows)
    }

    private fun persistProgress(snapshot: GameSnapshot) {
        viewModelScope.launch {
            gameRepository.updateProgress { progress ->
                progress.copy(
                    eggs = progress.eggs + snapshot.eggs,
                    extraLives = progress.extraLives + (snapshot.lives - 3).coerceAtLeast(0),
                    ownedHelmet = progress.ownedHelmet || snapshot.helmetActive,
                    ownedMagnet = progress.ownedMagnet || snapshot.magnetActive
                )
            }
        }
    }
}
