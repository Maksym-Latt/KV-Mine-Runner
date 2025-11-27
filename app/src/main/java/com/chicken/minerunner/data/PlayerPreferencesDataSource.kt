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

    val state: Flow<PreferencesState> = dataStore.data.map { prefs ->
        val eggs = prefs[Keys.eggs] ?: 0
        val musicEnabled = prefs[Keys.musicEnabled] ?: true
        val sfxEnabled = prefs[Keys.sfxEnabled] ?: true
        val itemLevels = prefs.asMap()
            .filterKeys { it.name.startsWith("item_") }
            .mapKeys { (key, _) -> key.name.removePrefix("item_").removeSuffix("_level") }
            .mapValues { (_, value) -> value as? Int ?: 0 }

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
            val current = prefs[Keys.eggs] ?: 0
            prefs[Keys.eggs] = current + amount
        }
    }

    suspend fun spendEggs(amount: Int): Boolean {
        if (amount <= 0) return false
        var success = false
        dataStore.edit { prefs ->
            val current = prefs[Keys.eggs] ?: 0
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
