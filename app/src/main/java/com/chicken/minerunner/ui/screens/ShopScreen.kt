package com.chicken.minerunner.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.minerunner.R
import com.chicken.dropper.ui.components.PrimaryButton
import com.chicken.minerunner.ui.components.GradientOutlinedText
import com.chicken.minerunner.ui.theme.CopperDark
import com.chicken.minerunner.ui.theme.CopperLight
import com.chicken.minerunner.ui.theme.Gold

private data class ShopItem(
    val title: String,
    val subtitle: String,
    val price: String,
    val image: Int
)

private val shopItems = listOf(
    ShopItem("MAGNET", "magnet for 10 seconds", "1499", R.drawable.item_magnet),
    ShopItem("HELMET", "invulnerability 3 seconds", "5499", R.drawable.item_helmet),
    ShopItem("EXTRA LIFE", "add additional extra life", "8499", R.drawable.item_extra_life)
)

@Composable
fun ShopScreen(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .background(CopperDark)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = Gold)
                }
                Text(
                    text = "SHOP",
                    style = MaterialTheme.typography.titleLarge,
                    color = Gold
                )
            }

            shopItems.forEach { item ->
                ShopCard(item)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun ShopCard(item: ShopItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CopperLight.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            GradientOutlinedText(
                text = "MINE",
                gradient = Brush.verticalGradient(listOf(Color.White, Gold)),
                outlineColor = Color(0xFF5D3B1D),
                fontSize = 32.sp
            )
            GradientOutlinedText(
                text = item.title,
                gradient = Brush.verticalGradient(listOf(Gold, Color.White)),
                outlineColor = Color(0xFF5D3B1D),
                fontSize = 30.sp
            )
            Text(
                text = item.subtitle,
                color = Color.White,
                fontSize = 14.sp
            )
            Image(
                painter = painterResource(id = item.image),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )
            PrimaryButton(
                text = item.price,
                onClick = { },
                style = com.chicken.dropper.ui.components.ChickenButtonStyle.Blue
            )
        }
    }
}
