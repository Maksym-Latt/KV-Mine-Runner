package com.chicken.minerunner.presentation.progress

data class ShopItemState(
    val id: String,
    val title: String,
    val subtitle: String,
    val image: Int,
    val level: Int = 0,
    val upgradePrices: List<Int> = emptyList(),
    val dynamicSubtitle: String = subtitle
) {
    val maxLevel: Int get() = upgradePrices.size
    val nextPrice: Int? get() = upgradePrices.getOrNull(level)
}

data class PlayerState(
    val eggs: Int = 0,
    val shopItems: List<ShopItemState> = emptyList(),
    val musicEnabled: Boolean = true,
    val sfxEnabled: Boolean = true
)

fun List<ShopItemState>.updateLevels(levels: Map<String, Int>): List<ShopItemState> = map { item ->
    val level = levels[item.id] ?: item.level
    item.copy(level = level)
}
