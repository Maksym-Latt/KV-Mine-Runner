package com.chicken.minerunner.domain.config

object GameConfig {
    const val worldColumns = 3
    const val maxLives = 3

    const val frameMs = 16L
    const val laneHeight = 220f
    const val cameraLerp = 0.12f
    const val preloadLanesAhead = 6
    const val initialLanesAhead = 8

    const val trolleyCollisionThreshold = 0.3f
    const val trolleyBounds = (worldColumns / 2) + 0.8f
    const val trolleyMinSpeed = 3f
    const val trolleyMaxSpeed = 5f
    const val maxTrolleysOnScreen = 3
    const val trolleySpawnChance = 0.9f
}
