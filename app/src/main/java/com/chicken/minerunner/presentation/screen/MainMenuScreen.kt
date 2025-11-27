package com.chicken.minerunner.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.minerunner.R
import com.chicken.minerunner.presentation.ui.components.CounterPill
import com.chicken.minerunner.presentation.ui.components.PrimaryButton
import com.chicken.minerunner.presentation.viewmodel.GameViewModel
import com.chicken.minerunner.ui.theme.OverlayDark
import com.chicken.minerunner.ui.theme.TextLight

@Composable
fun MainMenuScreen(
    onStart: () -> Unit,
    onUpgrade: () -> Unit,
    gameViewModel: GameViewModel
) {
    val uiState by gameViewModel.uiState.collectAsState()

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
                .background(OverlayDark),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RowCounters(coins = uiState.stats.coins, eggs = uiState.stats.eggs)
                Image(
                    painter = painterResource(id = R.drawable.chicken_title),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .size(260.dp),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = "Chicken\nMine Runner",
                    color = TextLight,
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier.padding(bottom = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PrimaryButton(
                    text = "Start",
                    modifier = Modifier.width(220.dp),
                    onClick = {
                        gameViewModel.startRun()
                        onStart()
                    }
                )
                PrimaryButton(
                    text = "Upgrades",
                    modifier = Modifier.width(220.dp),
                    onClick = onUpgrade
                )
            }
        }
    }
}

@Composable
private fun RowCounters(coins: Int, eggs: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
        CounterPill(iconRes = R.drawable.item_egg, value = eggs)
        CounterPill(iconRes = R.drawable.item_magnet, value = coins)
    }
}
