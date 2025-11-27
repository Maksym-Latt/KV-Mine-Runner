package com.chicken.minerunner.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.chicken.minerunner.R
import com.chicken.minerunner.core.domain.model.ItemType
import com.chicken.minerunner.ui.components.GradientButton
import com.chicken.minerunner.ui.components.GradientText
import com.chicken.minerunner.ui.theme.MineShadow
import com.chicken.minerunner.ui.shop.ShopViewModel

@Composable
fun ShopScreen(viewModel: ShopViewModel, onBack: () -> Unit) {
    val progress by viewModel.progress.collectAsState()
    val cards = listOf(
        ShopCard("MINE MAGNET", ItemType.MAGNET, R.drawable.item_magnet, progress.ownedMagnet),
        ShopCard("MINE HELMET", ItemType.HELMET, R.drawable.item_helmet, progress.ownedHelmet),
        ShopCard("MINE EXTRA LIFE", ItemType.EXTRA_LIFE, R.drawable.item_extra_life, progress.extraLives > 0)
    )
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GradientText(text = "UPGRADES", style = MaterialTheme.typography.displayLarge)
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(cards) { card ->
                ShopCardItem(card) { viewModel.purchase(card.type) }
            }
        }
        GradientButton(text = "LOBBY", modifier = Modifier.fillMaxWidth(0.8f)) { onBack() }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

data class ShopCard(val title: String, val type: ItemType, val icon: Int, val owned: Boolean)

@Composable
private fun ShopCardItem(card: ShopCard, onBuy: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MineShadow.copy(alpha = 0.85f)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GradientText(text = card.title, style = MaterialTheme.typography.headlineMedium)
            androidx.compose.foundation.Image(
                painter = painterResource(id = card.icon),
                contentDescription = null
            )
            val status = if (card.owned) "Selected" else "Buy"
            GradientButton(text = status, modifier = Modifier.fillMaxWidth()) { onBuy() }
            Text(
                text = if (card.owned) "Active" else "Unlock permanent bonus",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }
    }
}
