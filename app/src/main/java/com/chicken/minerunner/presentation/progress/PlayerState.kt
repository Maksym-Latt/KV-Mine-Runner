package com.chicken.minerunner.presentation.progress

data class ShopItemLevel(
    val description: String,
    val upgradePrice: Int?
)

data class ShopItemState(
    val id: String,
    val title: String,
    val subtitle: String,
    val image: Int,
    val level: Int = 1,
    val levels: List<ShopItemLevel> = emptyList()
) {
    val dynamicSubtitle: String
        get() = levels.getOrNull(level - 1)?.description ?: subtitle

    val nextPrice: Int?
        get() = levels.getOrNull(level - 1)?.upgradePrice

    val maxLevel: Int
        get() = levels.size
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
