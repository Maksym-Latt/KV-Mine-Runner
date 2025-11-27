package com.chicken.minerunner.domain.config

object GameConfig {

    val columns = listOf(-1, 0, 1)
    const val trackCount = 3

    var safeZoneHeightPx: Float = 0f
    var railwayHeightPx: Float = 0f

    const val worldColumns = 3
    const val maxLives = 3

    const val frameMs = 16L
    const val cameraLerp = 0.12f
    const val preloadLanesAhead = 6
    const val initialLanesAhead = 8

    const val trolleyCollisionThreshold = 0.3f
    const val trolleyBounds = (worldColumns / 2) + 0.8f
    const val trolleyMinSpeed = 1f
    const val trolleyMaxSpeed = 2f
    const val trolleySpawnChance = 0.7f
}
