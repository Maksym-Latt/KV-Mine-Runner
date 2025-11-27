package com.chicken.minerunner.ui.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chicken.minerunner.core.domain.model.GameProgress
import com.chicken.minerunner.core.domain.model.ItemType
import com.chicken.minerunner.core.domain.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val repository: GameRepository
) : ViewModel() {

    val progress: StateFlow<GameProgress> = repository.progress
        .map { it }
        .stateIn(viewModelScope, SharingStarted.Eagerly, GameProgress())

    fun purchase(type: ItemType) {
        viewModelScope.launch {
            repository.updateProgress { progress ->
                when (type) {
                    ItemType.MAGNET -> progress.copy(ownedMagnet = true)
                    ItemType.HELMET -> progress.copy(ownedHelmet = true)
                    ItemType.EXTRA_LIFE -> progress.copy(extraLives = progress.extraLives + 1)
                    ItemType.EGG -> progress
                }
            }
        }
    }
}
