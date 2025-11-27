package com.chicken.minerunner.core.domain.repository

import com.chicken.minerunner.core.domain.model.GameProgress
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    val progress: Flow<GameProgress>
    suspend fun updateProgress(transform: (GameProgress) -> GameProgress)
}
