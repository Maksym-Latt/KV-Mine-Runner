package com.chicken.minerunner.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.chicken.minerunner.data.repository.InMemoryGameConfigRepository
import com.chicken.minerunner.domain.model.ColliderCoefficients
import com.chicken.minerunner.domain.model.GameConfig
import com.chicken.minerunner.domain.usecase.GetGameConfigUseCase
import com.chicken.minerunner.domain.usecase.UpdateColliderCoefficientsUseCase
import com.chicken.minerunner.domain.usecase.UpdateTrolleySettingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameConfigViewModel(
    private val getGameConfigUseCase: GetGameConfigUseCase,
    private val updateColliderCoefficientsUseCase: UpdateColliderCoefficientsUseCase,
    private val updateTrolleySettingsUseCase: UpdateTrolleySettingsUseCase
) : ViewModel() {

    private val _config = MutableStateFlow(GameConfig())
    val config: StateFlow<GameConfig> = _config.asStateFlow()

    init {
        viewModelScope.launch {
            getGameConfigUseCase().collect { newConfig ->
                _config.value = newConfig
            }
        }
    }

    fun updateColliderCoefficients(coefficients: ColliderCoefficients) {
        viewModelScope.launch {
            updateColliderCoefficientsUseCase(coefficients)
        }
    }

    fun updateChickenCollider(value: Float) {
        updateColliderCoefficients(_config.value.colliderCoefficients.copy(chicken = value))
    }

    fun updateTrolleyCollider(value: Float) {
        updateColliderCoefficients(_config.value.colliderCoefficients.copy(trolley = value))
    }

    fun updateEggCollider(value: Float) {
        updateColliderCoefficients(_config.value.colliderCoefficients.copy(egg = value))
    }

    fun updateHelmetCollider(value: Float) {
        updateColliderCoefficients(_config.value.colliderCoefficients.copy(helmet = value))
    }

    fun updateMagnetCollider(value: Float) {
        updateColliderCoefficients(_config.value.colliderCoefficients.copy(magnet = value))
    }

    fun updateExtraLifeCollider(value: Float) {
        updateColliderCoefficients(_config.value.colliderCoefficients.copy(extraLife = value))
    }

    fun updateTrolleySpeed(value: Float) {
        viewModelScope.launch {
            updateTrolleySettingsUseCase(speed = value)
        }
    }

    fun updateMaxTrolleys(count: Int) {
        viewModelScope.launch {
            updateTrolleySettingsUseCase(maxTrolleysOnScreen = count)
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val repository = InMemoryGameConfigRepository()
                GameConfigViewModel(
                    getGameConfigUseCase = GetGameConfigUseCase(repository),
                    updateColliderCoefficientsUseCase = UpdateColliderCoefficientsUseCase(repository),
                    updateTrolleySettingsUseCase = UpdateTrolleySettingsUseCase(repository)
                )
            }
        }
    }
}
