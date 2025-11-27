package com.chicken.minerunner.domain.usecase

import com.chicken.minerunner.domain.model.ColliderCoefficients
import com.chicken.minerunner.domain.repository.GameConfigRepository

class UpdateColliderCoefficientsUseCase(
    private val repository: GameConfigRepository
) {
    suspend operator fun invoke(coefficients: ColliderCoefficients) {
        repository.updateColliderCoefficients(coefficients)
    }
}
