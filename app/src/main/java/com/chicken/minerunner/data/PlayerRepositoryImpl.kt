package com.chicken.minerunner.data

import com.chicken.minerunner.domain.repository.PlayerRepository
import com.chicken.minerunner.presentation.progress.PlayerState
import com.chicken.minerunner.presentation.progress.ShopItemState
import com.chicken.minerunner.presentation.progress.updateLevels
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerRepositoryImpl @Inject constructor(
    private val preferencesDataSource: PlayerPreferencesDataSource
) : PlayerRepository {

    private val baseItems = listOf(
        ShopItemState(
            id = "magnet",
            title = "MAGNET",
            subtitle = "magnet for 10 seconds",
            basePrice = 1499,
            image = com.chicken.minerunner.R.drawable.item_magnet
        ),
        ShopItemState(
            id = "helmet",
            title = "HELMET",
            subtitle = "invulnerability 3 seconds",
            basePrice = 5499,
            image = com.chicken.minerunner.R.drawable.item_helmet
        ),
        ShopItemState(
            id = "extra_life",
            title = "EXTRA LIFE",
            subtitle = "add additional extra life",
            basePrice = 8499,
            image = com.chicken.minerunner.R.drawable.item_extra_life,
            maxLevel = 5
        )
    )

    override val state: Flow<PlayerState> = preferencesDataSource.state.map { prefs ->
        PlayerState(
            eggs = prefs.eggs,
            shopItems = baseItems.updateLevels(prefs.itemLevels),
            musicEnabled = prefs.musicEnabled,
            sfxEnabled = prefs.sfxEnabled
        )
    }

    override suspend fun addEggs(amount: Int) {
        preferencesDataSource.addEggs(amount)
    }

    override suspend fun upgradeItem(id: String, price: Int, maxLevel: Int): Boolean {
        val success = preferencesDataSource.spendEggs(price)
        if (success) {
            val keyLevel = preferencesDataSource.state.map { it.itemLevels[id] ?: 0 }.firstOrNull() ?: 0
            val newLevel = (keyLevel + 1).coerceAtMost(maxLevel)
            preferencesDataSource.setItemLevel(id, newLevel)
        }
        return success
    }

    override suspend fun setItemLevel(id: String, level: Int) {
        preferencesDataSource.setItemLevel(id, level)
    }

    override suspend fun setMusicEnabled(enabled: Boolean) {
        preferencesDataSource.toggleMusic(enabled)
    }

    override suspend fun setSfxEnabled(enabled: Boolean) {
        preferencesDataSource.toggleSfx(enabled)
    }
}
