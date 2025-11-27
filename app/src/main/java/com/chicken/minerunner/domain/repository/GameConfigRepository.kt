package com.chicken.minerunner.domain.repository

import com.chicken.minerunner.domain.model.ColliderCoefficients
import com.chicken.minerunner.domain.model.GameConfig
import kotlinx.coroutines.flow.Flow

interface GameConfigRepository {
    fun observeConfig(): Flow<GameConfig>

    suspend fun updateColliderCoefficients(coefficients: ColliderCoefficients)

    suspend fun updateTrolleySettings(speed: Float? = null, maxTrolleysOnScreen: Int? = null)
}
