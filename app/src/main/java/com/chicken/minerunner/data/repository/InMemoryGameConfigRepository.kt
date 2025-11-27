package com.chicken.minerunner.data.repository

import com.chicken.minerunner.domain.model.ColliderCoefficients
import com.chicken.minerunner.domain.model.GameConfig
import com.chicken.minerunner.domain.repository.GameConfigRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class InMemoryGameConfigRepository : GameConfigRepository {

    private val configFlow = MutableStateFlow(GameConfig())

    override fun observeConfig(): Flow<GameConfig> = configFlow

    override suspend fun updateColliderCoefficients(coefficients: ColliderCoefficients) {
        val clamped = coefficients.clamped()
        configFlow.update { current ->
            current.copy(colliderCoefficients = clamped)
        }
    }

    override suspend fun updateTrolleySettings(speed: Float?, maxTrolleysOnScreen: Int?) {
        configFlow.update { current ->
            current.copy(
                trolleySpeed = speed?.coerceAtLeast(0f) ?: current.trolleySpeed,
                maxTrolleysOnScreen = maxTrolleysOnScreen?.coerceAtLeast(1) ?: current.maxTrolleysOnScreen
            )
        }
    }
}
