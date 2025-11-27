package com.chicken.minerunner.domain.model

/**
 * Normalized collider scales for game objects. Values are expected between 0f and 1f
 * where 1f represents the full visual size of the sprite.
 */
data class ColliderCoefficients(
    val chicken: Float = 0.85f,
    val trolley: Float = 0.9f,
    val egg: Float = 0.75f,
    val helmet: Float = 0.8f,
    val magnet: Float = 0.8f,
    val extraLife: Float = 0.85f
) {
    fun clamped(): ColliderCoefficients = copy(
        chicken = chicken.coerceIn(0f, 1f),
        trolley = trolley.coerceIn(0f, 1f),
        egg = egg.coerceIn(0f, 1f),
        helmet = helmet.coerceIn(0f, 1f),
        magnet = magnet.coerceIn(0f, 1f),
        extraLife = extraLife.coerceIn(0f, 1f)
    )
}
