package com.chicken.minerunner.domain.usecase

import com.chicken.minerunner.domain.repository.GameConfigRepository

class UpdateTrolleySettingsUseCase(
    private val repository: GameConfigRepository
) {
    suspend operator fun invoke(speed: Float? = null, maxTrolleysOnScreen: Int? = null) {
        repository.updateTrolleySettings(speed = speed, maxTrolleysOnScreen = maxTrolleysOnScreen)
    }
}
