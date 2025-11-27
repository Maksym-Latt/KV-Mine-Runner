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
            subtitle = "Magnet up to 10 seconds",
            basePrice = 1499,
            image = com.chicken.minerunner.R.drawable.item_magnet
        ),
        ShopItemState(
            id = "helmet",
            title = "HELMET",
            subtitle = "Invulnerability up to 10 seconds",
            basePrice = 5499,
            image = com.chicken.minerunner.R.drawable.item_helmet
        ),
        ShopItemState(
            id = "extra_life",
            title = "EXTRA LIFE",
            subtitle = "Increase extra life spawn",
            basePrice = 8499,
            image = com.chicken.minerunner.R.drawable.item_extra_life,
            maxLevel = 3
        )
    )

    private fun buildDynamicSubtitle(id: String, level: Int): String {
        return when (id) {
            "magnet" -> when (level) {
                0,1 -> "Magnet works for 5 seconds"
                2 -> "Magnet works for 7 seconds"
                3 -> "Magnet works for 10 seconds"
                else -> ""
            }
            "helmet" -> when (level) {
                0,1 -> "Invulnerability for 5 seconds"
                2 -> "Invulnerability for 7 seconds"
                3 -> "Invulnerability for 10 seconds"
                else -> ""
            }
            "extra_life" -> when (level) {
                0,1 -> "Extra life spawn chance 1%"
                2 -> "Extra life spawn chance 2%"
                3 -> "Extra life spawn chance 3%"
                else -> ""
            }
            else -> ""
        }
    }


    override val state: Flow<PlayerState> = preferencesDataSource.state.map { prefs ->

        val items = baseItems.map { base ->
            val level = prefs.itemLevels[base.id] ?: 0
            base.copy(
                level = level,
                dynamicSubtitle = buildDynamicSubtitle(base.id, level)
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
