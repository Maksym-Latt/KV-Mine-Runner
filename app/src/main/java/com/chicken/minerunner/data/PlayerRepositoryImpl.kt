package com.chicken.minerunner.data

import com.chicken.minerunner.domain.repository.PlayerRepository
import com.chicken.minerunner.presentation.progress.PlayerState
import com.chicken.minerunner.presentation.progress.ShopItemLevel
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

    private data class ShopItemDefinition(
        val id: String,
        val title: String,
        val subtitle: String,
        val image: Int,
        val levels: List<ShopItemLevel>
    )

    private val baseItems = listOf(
        ShopItemDefinition(
            id = "magnet",
            title = "MAGNET",
            subtitle = "Magnet up to 10 seconds",
            image = com.chicken.minerunner.R.drawable.item_magnet,
            levels = listOf(
                ShopItemLevel(description = "Magnet works for 5 seconds", upgradePrice = 500),
                ShopItemLevel(description = "Magnet works for 7 seconds", upgradePrice = 700),
                ShopItemLevel(description = "Magnet works for 10 seconds", upgradePrice = null)
            )
        ),
        ShopItemDefinition(
            id = "helmet",
            title = "HELMET",
            subtitle = "Invulnerability up to 10 seconds",
            image = com.chicken.minerunner.R.drawable.item_helmet,
            levels = listOf(
                ShopItemLevel(description = "Invulnerability for 5 seconds", upgradePrice = 500),
                ShopItemLevel(description = "Invulnerability for 7 seconds", upgradePrice = 700),
                ShopItemLevel(description = "Invulnerability for 10 seconds", upgradePrice = null)
            )
        ),
        ShopItemDefinition(
            id = "extra_life",
            title = "EXTRA LIFE",
            subtitle = "Increase extra life spawn",
            image = com.chicken.minerunner.R.drawable.item_extra_life,
            levels = listOf(
                ShopItemLevel(description = "Extra life spawn chance 1%", upgradePrice = 700),
                ShopItemLevel(description = "Extra life spawn chance 2%", upgradePrice = 1000),
                ShopItemLevel(description = "Extra life spawn chance 3%", upgradePrice = null)
            )
        )
    )


    override val state: Flow<PlayerState> = preferencesDataSource.state.map { prefs ->

        val items = baseItems.map { base ->
            val level = prefs.itemLevels[base.id] ?: 1
            ShopItemState(
                id = base.id,
                title = base.title,
                subtitle = base.subtitle,
                image = base.image,
                level = level,
                levels = base.levels
            )
        }

        PlayerState(
            eggs = prefs.eggs,
            shopItems = items,
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
