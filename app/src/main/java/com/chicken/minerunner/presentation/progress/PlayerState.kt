package com.chicken.minerunner.presentation.progress

data class ShopItemState(
    val id: String,
    val title: String,
    val subtitle: String,
    val basePrice: Int,
    val image: Int,
    val level: Int = 1,
    val maxLevel: Int = 3,
    val dynamicSubtitle: String = subtitle
) {
    val nextPrice: Int? get() = if (level >= maxLevel) null else basePrice * (level + 1)
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
