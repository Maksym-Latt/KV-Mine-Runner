package com.chicken.minerunner.domain.usecase

import com.chicken.minerunner.domain.model.GameConfig
import com.chicken.minerunner.domain.repository.GameConfigRepository
import kotlinx.coroutines.flow.Flow

class GetGameConfigUseCase(
    private val repository: GameConfigRepository
) {
    operator fun invoke(): Flow<GameConfig> = repository.observeConfig()
}
