package com.chicken.minerunner.core.domain.model

enum class LanePosition { LEFT, CENTER, RIGHT }

enum class TrackType { SAFE_ZONE, RAILWAY }

enum class ItemType { EGG, HELMET, MAGNET, EXTRA_LIFE }

data class LaneContent(
    val lane: LanePosition,
    val hasTrolley: Boolean = false,
    val item: ItemType? = null
)

data class TrackRow(
    val id: Int,
    val type: TrackType,
    val lanes: List<LaneContent>
)

data class GameProgress(
    val eggs: Int = 0,
    val extraLives: Int = 0,
    val ownedHelmet: Boolean = false,
    val ownedMagnet: Boolean = false
)

data class GameSnapshot(
    val rows: List<TrackRow> = emptyList(),
    val currentRowIndex: Int = 0,
    val lane: LanePosition = LanePosition.CENTER,
    val distance: Int = 0,
    val lives: Int = 3,
    val eggs: Int = 0,
    val magnetActive: Boolean = false,
    val helmetActive: Boolean = false,
    val isPaused: Boolean = false,
    val gameOver: Boolean = false
)

enum class SwipeDirection { LEFT, RIGHT, FORWARD }
