package com.chicken.minerunner.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.playerDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "player_prefs"
)

@Singleton
class PlayerPreferencesDataSource @Inject constructor(
    @ApplicationContext context: Context
) {

    private val dataStore = context.playerDataStore

    private val defaultEggs = 188880

    private object Keys {
        val eggs = intPreferencesKey("eggs")
        val musicEnabled = booleanPreferencesKey("music_enabled")
        val sfxEnabled = booleanPreferencesKey("sfx_enabled")

        fun itemLevel(id: String): Preferences.Key<Int> = intPreferencesKey("item_${'$'}id_level")
    }

    data class PreferencesState(
        val eggs: Int = 0,
        val itemLevels: Map<String, Int> = emptyMap(),
        val musicEnabled: Boolean = true,
        val sfxEnabled: Boolean = true
    )

    private val baseItemIds = listOf("magnet", "helmet", "extra_life")

    val state: Flow<PreferencesState> = dataStore.data.map { prefs ->
        val eggs = prefs[Keys.eggs] ?: defaultEggs
        val musicEnabled = prefs[Keys.musicEnabled] ?: true
        val sfxEnabled = prefs[Keys.sfxEnabled] ?: true

        val itemLevels = mutableMapOf<String, Int>()
        baseItemIds.forEach { id ->
            val key = Keys.itemLevel(id)
            val level = prefs[key] ?: 1
            itemLevels[id] = level
        }

        PreferencesState(
            eggs = eggs,
            itemLevels = itemLevels,
            musicEnabled = musicEnabled,
            sfxEnabled = sfxEnabled
        )
    }

    suspend fun addEggs(amount: Int) {
        if (amount <= 0) return
        dataStore.edit { prefs ->
            val current = prefs[Keys.eggs] ?: defaultEggs
            prefs[Keys.eggs] = current + amount
        }
    }

    suspend fun spendEggs(amount: Int): Boolean {
        if (amount <= 0) return false
        var success = false
        dataStore.edit { prefs ->
            val current = prefs[Keys.eggs] ?: defaultEggs
            if (current >= amount) {
                prefs[Keys.eggs] = current - amount
                success = true
            }
        }
        return success
    }

    suspend fun setItemLevel(id: String, level: Int) {
        dataStore.edit { prefs ->
            prefs[Keys.itemLevel(id)] = level
        }
    }

    suspend fun toggleMusic(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[Keys.musicEnabled] = enabled }
    }

    suspend fun toggleSfx(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[Keys.sfxEnabled] = enabled }
    }

}
