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
    private var itemLevels: Map<String, Int> = emptyMap()

    private val _ui = MutableStateFlow(startState())
    val ui = _ui.asStateFlow()

    private var ticker: Job? = null
    private var reward = false

    init {
        viewModelScope.launch {
            repo.state.collect { state ->
                itemLevels = state.shopItems.associate { it.id to it.level }
            }
        }
    }

    private fun magnetDurationMs(): Long = when (itemLevels["magnet"] ?: 1) {
        1 -> 5_000L
        2 -> 7_000L
        3 -> 10_000L
        else -> 5_000L
    }

    private fun helmetDurationMs(): Long = when (itemLevels["helmet"] ?: 1) {
        1 -> 5_000L
        2 -> 7_000L
        3 -> 10_000L
        else -> 5_000L
    }

    private fun extraLifeSpawnChance(): Int = when (itemLevels["extra_life"] ?: 1) {
        1 -> 1
        2 -> 2
        3 -> 3
        else -> 1
    }

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
                SwipeDirection.Left -> moveSide(st, -1)
                SwipeDirection.Right -> moveSide(st, +1)
                SwipeDirection.Forward -> forward(st)
            }
        }
    }

    private fun moveSide(st: GameUiState, delta: Int): GameUiState {
        val currentIdx = GameConfig.columns.indexOf(st.chickenColumn)
        val newIdx = (currentIdx + delta).coerceIn(0, GameConfig.columns.lastIndex)
        val newCol = GameConfig.columns[newIdx]

        val laneIndex = st.segments.indexOfFirst { it.index == st.chickenLane }
        val lane = st.segments.getOrNull(laneIndex)

        val (afterPick, updatedLane) = pick(st.stats, lane, newCol)

        val newSegments = st.segments.toMutableList()
        if (laneIndex >= 0 && updatedLane != null) {
            newSegments[laneIndex] = updatedLane
        }

        return st.copy(
            chickenColumn = newCol,
            stats = afterPick,
            segments = newSegments
        )
    }

    private fun forward(st: GameUiState): GameUiState {
        val next = st.chickenLane + 1
        val segs = fill(st.segments, next).toMutableList()
        val laneIndex = segs.indexOfFirst { it.index == next }

        val (afterPickStats, updatedLaneAfterPick) = pick(st.stats, segs.getOrNull(laneIndex), st.chickenColumn)
        if (laneIndex >= 0) {
            updatedLaneAfterPick?.let { segs[laneIndex] = it }
        }

        val (afterCollisionStats, updatedLaneAfterCollision) = collide(
            afterPickStats,
            segs.getOrNull(laneIndex),
            st.chickenColumn
        )
        if (laneIndex >= 0) {
            updatedLaneAfterCollision?.let { segs[laneIndex] = it }
        }
        val status =
            if (afterCollisionStats.lives <= 0) GameStatus.GameOver else st.status

        if (status is GameStatus.GameOver && !reward) {
            reward = true
            viewModelScope.launch {
                repo.addEggs(afterCollisionStats.eggs)
            }
        }

        return st.copy(
            chickenLane = next,
            segments = segs,
            stats = afterCollisionStats.copy(distance = next),
            status = status
        )
    }

    private fun pick(stats: GameStats, lane: LaneSegment?, col: Int): Pair<GameStats, LaneSegment?> {
        if (lane == null) return stats to null

        var updatedStats = stats
        var updated = false

        val updatedItems = lane.items.map { item ->
            val collectEggWithMagnet = item.type == ItemType.Egg && updatedStats.magnetActiveMs > 0
            val shouldCollect = when {
                !item.active -> false


                item.column == col -> true

                item.type == ItemType.Egg && updatedStats.magnetActiveMs > 0 -> true

                else -> false
            }

            if (shouldCollect) {
                updated = true
                updatedStats = applyItemEffect(updatedStats, item)
                item.copy(active = false)
            } else {
                item
            }
        }

        return if (updated) updatedStats to lane.copy(items = updatedItems) else stats to null
    }

    private fun applyItemEffect(stats: GameStats, item: GameItem): GameStats {
        return when (item.type) {
            ItemType.Egg -> stats.copy(eggs = stats.eggs + 1)
            ItemType.Magnet -> {
                val duration = magnetDurationMs()
                if (duration > 0) stats.copy(magnetActiveMs = duration, magnetDurationMs = duration) else stats
            }

            ItemType.Helmet -> {
                val duration = helmetDurationMs()
                if (duration > 0) stats.copy(helmetActiveMs = duration, helmetDurationMs = duration) else stats
            }

            ItemType.ExtraLife -> stats.copy(lives = (stats.lives + 1).coerceAtMost(GameConfig.maxLives))
        }
    }


    private fun collide(stats: GameStats, lane: LaneSegment?, col: Int): Pair<GameStats, LaneSegment?> {
        if (lane == null || lane.type != LaneType.Railway) return stats to null
        val tr = lane.trolley ?: return stats to null

        val dx = abs(col - tr.position)
        val collided = dx < GameConfig.trolleyCollisionThreshold

        if (!collided) return stats to null

        if (stats.helmetActiveMs > 0) {
            return stats.copy(
                helmetActiveMs = 0,
                helmetDurationMs = 0
            ) to lane.copy(trolley = null)
        }

        val updatedStats = stats.copy(lives = stats.lives - 1)
        return updatedStats to lane.copy(trolley = null)
    }


    private fun tick() {
        ticker?.cancel()
        ticker = viewModelScope.launch {
            while (true) {
                delay(GameConfig.frameMs)
                _ui.update { st ->
                    if (st.status != GameStatus.Running)
                        return@update st.copy(cameraOffset = cam(st))

                    val moved = move(st)
                    val currentLane = moved.segments.firstOrNull { it.index == moved.chickenLane }
                    val (pickedStats, pickedLane) = pick(moved.stats, currentLane, moved.chickenColumn)
                    val laneAfterPick = pickedLane ?: currentLane

                    val (collidedStats, collidedLane) = collide(
                        pickedStats,
                        laneAfterPick,
                        moved.chickenColumn
                    )

                    if (collidedStats.lives <= 0) {
                        // reward once
                        if (!reward) {
                            reward = true
                            viewModelScope.launch { repo.addEggs(collidedStats.eggs) }
                        }

                        return@update moved.copy(
                            stats = collidedStats,
                            status = GameStatus.GameOver
                        )
                    }

                    val finalLane = collidedLane ?: laneAfterPick
                    val updatedSegments = finalLane?.let { lane ->
                        moved.segments.map { if (it.index == lane.index) lane else it }
                    } ?: moved.segments

                    val timedStats = timers(collidedStats)
                    moved.copy(
                        segments = updatedSegments,
                        stats = timedStats,
                        cameraOffset = cam(moved)
                    )
                }
            }
        }
    }

    private fun timers(s: GameStats): GameStats {
        fun Long.drop() = (this - GameConfig.frameMs).coerceAtLeast(0)
        val magnetLeft = s.magnetActiveMs.drop()
        val helmetLeft = s.helmetActiveMs.drop()
        return s.copy(
            magnetActiveMs = magnetLeft,
            helmetActiveMs = helmetLeft,
            magnetDurationMs = if (magnetLeft == 0L) 0 else s.magnetDurationMs,
            helmetDurationMs = if (helmetLeft == 0L) 0 else s.helmetDurationMs
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

        val spawn = Random.nextFloat() < GameConfig.trolleySpawnChance
        val t = if (spawn)
            Trolley(
                position = GameConfig.columns.random().toFloat(),
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

        val life = extraLifeSpawnChance()

        return when {
            r < 90 -> GameItem(c, ItemType.Egg)
            r < 92 -> GameItem(c, ItemType.Helmet)
            r < 94 -> GameItem(c, ItemType.Magnet)
            r < 94 + life -> GameItem(c, ItemType.ExtraLife)
            else -> null
        }
    }

    private fun startState(): GameUiState {
        val s = buildList {
            add(LaneSegment(-1, LaneType.Railway, null, emptyList()))
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
        val target = h(st, st.chickenLane)

        val lagOffset = GameConfig.railwayHeightPx * 0.35f

        val smoothTarget = target - lagOffset

        val current = st.cameraOffset

        val smoothMs = 260f

        return smoothDamp(
            current = current,
            target = smoothTarget,
            smoothTimeMs = smoothMs,
            frameMs = GameConfig.frameMs.toFloat()
        )
    }


    private fun smoothDamp(current: Float, target: Float, smoothTimeMs: Float, frameMs: Float): Float {
        val t = frameMs / smoothTimeMs
        val factor = 1f - kotlin.math.exp(-t)
        return current + (target - current) * factor
    }
}