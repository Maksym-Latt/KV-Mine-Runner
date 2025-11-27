package com.chicken.minerunner.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.minerunner.R
import com.chicken.minerunner.presentation.ui.components.CounterPill
import com.chicken.minerunner.presentation.ui.components.PrimaryButton
import com.chicken.minerunner.ui.theme.Amber
import com.chicken.minerunner.ui.theme.AmberDark
import com.chicken.minerunner.ui.theme.OverlayDark
import com.chicken.minerunner.ui.theme.TextLight

private val upgradeItems = listOf(
    UpgradeUi("Mine Magnet", R.drawable.item_magnet, "magnet for 10 seconds", 1499),
    UpgradeUi("Mine Helmet", R.drawable.item_helmet, "invulnerability 3 seconds", 5499),
    UpgradeUi("Extra Life", R.drawable.item_extra_life, "add additional extra life", 8499)
)

data class UpgradeUi(
    val title: String,
    val image: Int,
    val description: String,
    val price: Int
)

@Composable
fun UpgradeScreen(onBack: () -> Unit) {
    val selectedIndex = remember { mutableIntStateOf(0) }
    val item = upgradeItems[selectedIndex.intValue]

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_menu),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(OverlayDark.copy(alpha = 0.85f))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CounterPill(iconRes = R.drawable.item_egg, value = 1500)
                PrimaryButton(text = "Back", onClick = onBack, modifier = Modifier.width(120.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))
            UpgradeCarousel(selectedIndex.intValue) { delta ->
                val next = (selectedIndex.intValue + delta + upgradeItems.size) % upgradeItems.size
                selectedIndex.intValue = next
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = item.title.uppercase(),
                color = TextLight,
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 28.sp
            )
            Text(
                text = item.description,
                color = TextLight,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            PriceTag(price = item.price)
            Spacer(modifier = Modifier.height(12.dp))
            PrimaryButton(text = "Upgrade", onClick = { /* TODO buy */ }, modifier = Modifier.width(220.dp))
        }
    }
}

@Composable
private fun UpgradeCarousel(index: Int, onChange: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ArrowButton(direction = "<") { onChange(-1) }
        Box(
            modifier = Modifier
                .weight(1f)
                .height(260.dp)
                .padding(horizontal = 12.dp)
                .shadow(16.dp, RoundedCornerShape(16.dp))
                .background(Color(0xFF3A2E23), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = upgradeItems[index].image),
                contentDescription = null,
                modifier = Modifier.size(180.dp),
                contentScale = ContentScale.Fit
            )
        }
        ArrowButton(direction = ">") { onChange(1) }
    }
}

@Composable
private fun ArrowButton(direction: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .shadow(12.dp, RoundedCornerShape(12.dp))
            .background(Brush.verticalGradient(listOf(Amber, AmberDark)), RoundedCornerShape(12.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = direction, color = TextLight, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun PriceTag(price: Int) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .background(Color(0xFF2C2218), RoundedCornerShape(12.dp))
            .border(3.dp, Color(0xFF8B6B3B), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(painter = painterResource(id = R.drawable.item_egg), contentDescription = null, modifier = Modifier.size(28.dp))
        Text(text = price.toString(), color = TextLight, style = MaterialTheme.typography.titleLarge)
    }
}
