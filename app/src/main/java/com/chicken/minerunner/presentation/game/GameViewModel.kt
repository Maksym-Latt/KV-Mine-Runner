package com.chicken.minerunner.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chicken.minerunner.domain.config.GameConfig
import com.chicken.minerunner.domain.model.GameItem
import com.chicken.minerunner.domain.model.GameStatus
import com.chicken.minerunner.domain.model.GameUiState
import com.chicken.minerunner.domain.model.ItemType
import com.chicken.minerunner.domain.model.LaneSegment
import com.chicken.minerunner.domain.model.LaneType
import com.chicken.minerunner.domain.model.SwipeDirection
import com.chicken.minerunner.domain.model.Trolley
import com.chicken.minerunner.domain.repository.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.random.Random

@HiltViewModel
class GameViewModel @javax.inject.Inject constructor(
    private val playerRepository: PlayerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(createInitialState())
    val uiState = _uiState.asStateFlow()

    private var tickerJob: Job? = null
    private var runRewardDelivered: Boolean = false

    fun startGame() {
        _uiState.value = createInitialState().copy(status = GameStatus.Running)
        runRewardDelivered = false
        startTicker()
    }

    fun pauseGame() {
        _uiState.update { state ->
            if (state.status is GameStatus.GameOver) state else state.copy(status = GameStatus.Paused)
        }
    }

    fun resumeGame() {
        _uiState.update { state ->
            if (state.status is GameStatus.GameOver) state else state.copy(status = GameStatus.Running)
        }
    }

    fun swipe(direction: SwipeDirection) {
        _uiState.update { state ->
            if (state.status !is GameStatus.Running) return@update state
            when (direction) {
                SwipeDirection.Left -> state.copy(chickenColumn = (state.chickenColumn - 1).coerceAtLeast(-(GameConfig.worldColumns / 2)))
                SwipeDirection.Right -> state.copy(chickenColumn = (state.chickenColumn + 1).coerceAtMost(GameConfig.worldColumns / 2))
                SwipeDirection.Forward -> advanceLane(state)
            }
        }
    }

    private fun advanceLane(current: GameUiState): GameUiState {
        val nextLane = current.chickenLane + 1
        val segments = ensureSegmentCount(current.segments, nextLane)
        val lane = segments.firstOrNull { it.index == nextLane }
        val updatedStats = handleItemPickup(current.stats, lane, current.chickenColumn)
        val statsAfterCollision = lane?.let { checkCollision(updatedStats, it, current.chickenColumn) } ?: updatedStats

        val newStatus = if (statsAfterCollision.lives <= 0) GameStatus.GameOver else current.status
        if (newStatus is GameStatus.GameOver && !runRewardDelivered) {
            runRewardDelivered = true
            viewModelScope.launch {
                playerRepository.addEggs(statsAfterCollision.eggs)
            }
        }
        return current.copy(
            chickenLane = nextLane,
            segments = segments,
            stats = statsAfterCollision.copy(distance = nextLane),
            status = newStatus
        )
    }

    private fun handleItemPickup(stats: com.chicken.minerunner.domain.model.GameStats, lane: LaneSegment?, column: Int): com.chicken.minerunner.domain.model.GameStats {
        val item = lane?.items?.firstOrNull { it.active && it.column == column }
        return when (item?.type) {
            ItemType.Egg -> stats.copy(eggs = stats.eggs + 1)
            ItemType.Magnet -> stats.copy(magnetActiveMs = 10_000)
            ItemType.Helmet -> stats.copy(helmetActiveMs = 3_000)
            ItemType.ExtraLife -> stats.copy(lives = (stats.lives + 1).coerceAtMost(GameConfig.maxLives))
            else -> stats
        }
    }

    private fun checkCollision(stats: com.chicken.minerunner.domain.model.GameStats, lane: LaneSegment, column: Int): com.chicken.minerunner.domain.model.GameStats {
        if (lane.type != LaneType.Railway) return stats
        val trolley = lane.trolley ?: return stats
        val hitsHelmet = stats.helmetActiveMs > 0
        val playerX = column.toFloat()
        val trolleyX = trolley.position
        return if (abs(playerX - trolleyX) < GameConfig.trolleyCollisionThreshold) {
            if (hitsHelmet) stats.copy(helmetActiveMs = 0) else stats.copy(lives = stats.lives - 1)
        } else stats
    }

    private fun startTicker() {
        tickerJob?.cancel()
        tickerJob = viewModelScope.launch {
            while (true) {
                delay(GameConfig.frameMs)
                _uiState.update { state ->
                    if (state.status != GameStatus.Running) return@update state.copy(cameraOffset = lerpCamera(state))

                    val advanced = updateTrolleys(state)
                    val timedStats = updateTimers(advanced.stats)
                    advanced.copy(
                        stats = timedStats,
                        cameraOffset = lerpCamera(advanced)
                    )
                }
            }
        }
    }

    private fun updateTimers(stats: com.chicken.minerunner.domain.model.GameStats): com.chicken.minerunner.domain.model.GameStats {
        fun Long.step(): Long = (this - GameConfig.frameMs).coerceAtLeast(0)
        return stats.copy(
            magnetActiveMs = stats.magnetActiveMs.step(),
            helmetActiveMs = stats.helmetActiveMs.step()
        )
    }

    private fun updateTrolleys(state: GameUiState): GameUiState {
        val updatedSegments = state.segments.map { segment ->
            if (segment.trolley == null) return@map segment
            val bounds = GameConfig.trolleyBounds
            val newPos = segment.trolley.position + segment.trolley.direction * (segment.trolley.speed / 60f)
            val wrapped = when {
                newPos > bounds -> -bounds
                newPos < -bounds -> bounds
                else -> newPos
            }
            segment.copy(trolley = segment.trolley.copy(position = wrapped))
        }
        return state.copy(segments = updatedSegments)
    }

    private fun lerpCamera(state: GameUiState): Float {
        val target = state.chickenLane * GameConfig.laneHeight
        val current = state.cameraOffset
        return current + (target - current) * GameConfig.cameraLerp
    }

    private fun ensureSegmentCount(existing: List<LaneSegment>, nextLane: Int): List<LaneSegment> {
        val mutable = existing.toMutableList()
        val highest = existing.maxOfOrNull { it.index } ?: -1
        var cursor = highest
        while (cursor < nextLane + GameConfig.preloadLanesAhead) {
            val newIndex = cursor + 1
            mutable.add(generateSegment(newIndex, mutable))
            cursor++
        }
        return mutable
    }

    private fun generateSegment(index: Int, existing: List<LaneSegment>): LaneSegment {
        if (index == 0) {
            return LaneSegment(index, LaneType.SafeZone, trolley = null, items = emptyList())
        }

        // every 6th lane is a double safe zone island
        if (index % 6 == 0) {
            return LaneSegment(index, LaneType.SafeZone, trolley = null, items = emptyList())
        }

        val type = LaneType.Railway
        val activeTrolleys = existing.count { it.trolley != null }
        val trolley = if (activeTrolleys < GameConfig.maxTrolleysOnScreen && Random.nextFloat() < GameConfig.trolleySpawnChance) {
            Trolley(
                position = Random.nextInt(-1, 2).toFloat(),
                direction = listOf(-1, 1).random(),
                speed = Random.nextFloat() * (GameConfig.trolleyMaxSpeed - GameConfig.trolleyMinSpeed) + GameConfig.trolleyMinSpeed
            )
        } else null
        val items = listOfNotNull(spawnItem())
        return LaneSegment(index, type, trolley, items)
    }

    private fun spawnItem(): GameItem? {
        val roll = Random.nextInt(0, 100)
        val column = Random.nextInt(-(GameConfig.worldColumns / 2), GameConfig.worldColumns / 2 + 1)
        return when {
            roll < 90 -> GameItem(column, ItemType.Egg)
            roll < 94 -> GameItem(column, ItemType.Helmet)
            roll < 98 -> GameItem(column, ItemType.Magnet)
            roll < 100 -> GameItem(column, ItemType.ExtraLife)
            else -> null
        }
    }

    private fun createInitialState(): GameUiState {
        val initialSegments = buildList {
            add(LaneSegment(index = 0, type = LaneType.SafeZone, trolley = null, items = emptyList()))
            addAll((1..GameConfig.initialLanesAhead).map { generateSegment(it, this) })
        }
        return GameUiState(
            segments = initialSegments,
            chickenColumn = 0,
            chickenLane = 0,
            status = GameStatus.Ready,
            cameraOffset = 0f
        )
    }
}
