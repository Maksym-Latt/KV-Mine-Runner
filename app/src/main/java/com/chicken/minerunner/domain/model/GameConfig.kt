package com.chicken.minerunner.domain.model

/**
 * Tunable gameplay settings exposed for quick balancing.
 */
data class GameConfig(
    val colliderCoefficients: ColliderCoefficients = ColliderCoefficients(),
    val trolleySpeed: Float = 2.5f,
    val maxTrolleysOnScreen: Int = 3
)
