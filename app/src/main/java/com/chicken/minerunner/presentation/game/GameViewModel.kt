package com.chicken.minerunner.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chicken.minerunner.domain.config.GameConfig
import com.chicken.minerunner.domain.model.GameItem
import com.chicken.minerunner.domain.model.GameStats
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
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val repo: PlayerRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(startState())
    val ui = _ui.asStateFlow()

    private var ticker: Job? = null
    private var reward = false

    fun start() {
        _ui.value = startState().copy(status = GameStatus.Running)
        reward = false
        tick()
    }

    fun pause() {
        _ui.update {
            if (it.status is GameStatus.GameOver) it else it.copy(status = GameStatus.Paused)
        }
    }

    fun resume() {
        _ui.update {
            if (it.status is GameStatus.GameOver) it else it.copy(status = GameStatus.Running)
        }
    }

    fun swipe(dir: SwipeDirection) {
        _ui.update { st ->
            if (st.status !is GameStatus.Running) return@update st
            when (dir) {
                SwipeDirection.Left -> {
                    val idx = GameConfig.columns.indexOf(st.chickenColumn)
                    val nextIdx = (idx - 1).coerceAtLeast(0)
                    st.copy(chickenColumn = GameConfig.columns[nextIdx])
                }

                SwipeDirection.Right -> {
                    val idx = GameConfig.columns.indexOf(st.chickenColumn)
                    val nextIdx = (idx + 1).coerceAtMost(GameConfig.columns.lastIndex)
                    st.copy(chickenColumn = GameConfig.columns[nextIdx])
                }
                SwipeDirection.Forward ->
                    forward(st)
            }
        }
    }

    private fun forward(st: GameUiState): GameUiState {
        val next = st.chickenLane + 1
        val segs = fill(st.segments, next)
        val lane = segs.firstOrNull { it.index == next }
        val s1 = pick(st.stats, lane, st.chickenColumn)
        val s2 = lane?.let { collide(s1, it, st.chickenColumn) } ?: s1
        val status =
            if (s2.lives <= 0) GameStatus.GameOver else st.status

        if (status is GameStatus.GameOver && !reward) {
            reward = true
            viewModelScope.launch {
                repo.addEggs(s2.eggs)
            }
        }

        return st.copy(
            chickenLane = next,
            segments = segs,
            stats = s2.copy(distance = next),
            status = status
        )
    }

    private fun pick(stats: GameStats, lane: LaneSegment?, col: Int): GameStats {
        val item = lane?.items?.firstOrNull { it.active && it.column == col }
        return when (item?.type) {
            ItemType.Egg -> stats.copy(eggs = stats.eggs + 1)
            ItemType.Magnet -> stats.copy(magnetActiveMs = 10000)
            ItemType.Helmet -> stats.copy(helmetActiveMs = 3000)
            ItemType.ExtraLife -> stats.copy(lives = (stats.lives + 1).coerceAtMost(GameConfig.maxLives))
            else -> stats
        }
    }

    private fun collide(stats: GameStats, lane: LaneSegment, col: Int): GameStats {
        if (lane.type != LaneType.Railway) return stats
        val tr = lane.trolley ?: return stats
        val dx = abs(col - tr.position)
        val t = 0.8f
        return if (dx < t) {
            if (stats.helmetActiveMs > 0) stats.copy(helmetActiveMs = 0)
            else stats.copy(lives = stats.lives - 1)
        } else stats
    }

    private fun tick() {
        ticker?.cancel()
        ticker = viewModelScope.launch {
            while (true) {
                delay(GameConfig.frameMs)
                _ui.update { st ->
                    if (st.status != GameStatus.Running)
                        return@update st.copy(cameraOffset = cam(st))

                    val a = move(st)
                    val b = timers(a.stats)
                    a.copy(
                        stats = b,
                        cameraOffset = cam(a)
                    )
                }
            }
        }
    }

    private fun timers(s: GameStats): GameStats {
        fun Long.drop() = (this - GameConfig.frameMs).coerceAtLeast(0)
        return s.copy(
            magnetActiveMs = s.magnetActiveMs.drop(),
            helmetActiveMs = s.helmetActiveMs.drop()
        )
    }

    private fun move(st: GameUiState): GameUiState {
        val segs = st.segments.map { lane ->
            if (lane.trolley == null) lane
            else {
                val b = GameConfig.trolleyBounds
                val n = lane.trolley.position + lane.trolley.direction * (lane.trolley.speed / 60f)
                val w = when {
                    n > b -> -b
                    n < -b -> b
                    else -> n
                }
                lane.copy(trolley = lane.trolley.copy(position = w))
            }
        }
        return st.copy(segments = segs)
    }

    private fun fill(list: List<LaneSegment>, lane: Int): List<LaneSegment> {
        val m = list.toMutableList()
        var h = list.maxOfOrNull { it.index } ?: -1
        while (h < lane + GameConfig.preloadLanesAhead) {
            val i = h + 1
            m.add(make(i, m))
            h++
        }
        return m
    }

    private fun make(i: Int, ex: List<LaneSegment>): LaneSegment {
        if (i == 0) return LaneSegment(i, LaneType.SafeZone, null, emptyList())
        if (i % 6 == 0) return LaneSegment(i, LaneType.SafeZone, null, emptyList())

        val v = ex.takeLast(GameConfig.preloadLanesAhead + 5).count { it.trolley != null }
        val t =
            if (v < GameConfig.maxTrolleysOnScreen && Random.nextFloat() < GameConfig.trolleySpawnChance)
                Trolley(
                    position = Random.nextInt(-1, 2).toFloat(),
                    direction = listOf(-1, 1).random(),
                    speed = Random.nextFloat() *
                            (GameConfig.trolleyMaxSpeed - GameConfig.trolleyMinSpeed) +
                            GameConfig.trolleyMinSpeed
                )
            else null

        val item = loot()
        return LaneSegment(i, LaneType.Railway, t, listOfNotNull(item))
    }

    private fun loot(): GameItem? {
        val r = Random.nextInt(0, 100)
        val c = GameConfig.columns.random()
        return when {
            r < 90 -> GameItem(c, ItemType.Egg)
            r < 94 -> GameItem(c, ItemType.Helmet)
            r < 98 -> GameItem(c, ItemType.Magnet)
            r < 100 -> GameItem(c, ItemType.ExtraLife)
            else -> null
        }
    }

    private fun startState(): GameUiState {
        val s = buildList {
            add(LaneSegment(0, LaneType.SafeZone, null, emptyList()))
            addAll((1..GameConfig.initialLanesAhead).map { make(it, this) })
        }
        return GameUiState(
            segments = s,
            chickenColumn = 0,
            chickenLane = 0,
            status = GameStatus.Ready,
            cameraOffset = 0f
        )
    }

    private fun h(st: GameUiState, lane: Int): Float {
        var sum = 0f
        st.segments.forEach {
            if (it.index >= lane) return sum
            sum += if (it.type == LaneType.SafeZone) GameConfig.safeZoneHeightPx
            else GameConfig.railwayHeightPx
        }
        return sum
    }

    private fun cam(st: GameUiState): Float {
        val t = h(st, st.chickenLane)
        val c = st.cameraOffset
        return c + (t - c) * GameConfig.cameraLerp
    }
}