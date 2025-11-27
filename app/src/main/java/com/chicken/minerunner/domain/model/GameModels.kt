package com.chicken.minerunner.domain.model

enum class LaneType { SafeZone, Railway }

data class GameItem(
    val column: Int,
    val type: ItemType,
    val active: Boolean = true
)

data class LaneSegment(
    val index: Int,
    val type: LaneType,
    val trolley: Trolley?,
    val items: List<GameItem>
)

data class Trolley(
    val position: Float,
    val direction: Int,
    val speed: Float
)

enum class ItemType { Egg, Magnet, Helmet, ExtraLife }

enum class SwipeDirection { Left, Right, Forward }

data class GameStats(
    val distance: Int = 0,
    val eggs: Int = 0,
    val lives: Int = 3,
    val magnetActiveMs: Long = 0L,
    val helmetActiveMs: Long = 0L
)

sealed class GameStatus {
    object Ready : GameStatus()
    object Running : GameStatus()
    object Paused : GameStatus()
    object GameOver : GameStatus()
}

data class GameUiState(
    val segments: List<LaneSegment> = emptyList(),
    val chickenColumn: Int = 0,
    val chickenLane: Int = 0,
    val cameraOffset: Float = 0f,
    val status: GameStatus = GameStatus.Ready,
    val stats: GameStats = GameStats()
)
