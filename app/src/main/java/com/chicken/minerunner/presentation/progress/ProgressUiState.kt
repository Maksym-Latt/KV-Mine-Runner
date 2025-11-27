package com.chicken.minerunner.presentation.progress

data class ProgressUiState(
    val eggs: Int = 0,
    val shopItems: List<ShopItemState> = emptyList(),
    val currentItemIndex: Int = 0,
    val musicEnabled: Boolean = true,
    val sfxEnabled: Boolean = true,
    val settingsVisible: Boolean = false,
    val message: String? = null
)
