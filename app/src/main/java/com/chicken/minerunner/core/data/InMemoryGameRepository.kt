package com.chicken.minerunner.core.data

import com.chicken.minerunner.core.domain.model.GameProgress
import com.chicken.minerunner.core.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryGameRepository : GameRepository {
    private val progressState = MutableStateFlow(GameProgress())
    override val progress: Flow<GameProgress> = progressState.asStateFlow()

    override suspend fun updateProgress(transform: (GameProgress) -> GameProgress) {
        progressState.value = transform(progressState.value)
    }
}
