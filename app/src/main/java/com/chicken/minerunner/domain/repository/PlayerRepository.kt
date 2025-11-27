package com.chicken.minerunner.domain.repository

import com.chicken.minerunner.presentation.progress.PlayerState
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    val state: Flow<PlayerState>

    suspend fun addEggs(amount: Int)

    suspend fun upgradeItem(id: String, price: Int, maxLevel: Int): Boolean

    suspend fun setItemLevel(id: String, level: Int)

    suspend fun setMusicEnabled(enabled: Boolean)

    suspend fun setSfxEnabled(enabled: Boolean)
}
