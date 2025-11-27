package com.chicken.minerunner.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun GameOverScreen(
    onRetry: () -> Unit,
    onLobby: () -> Unit,
    gameViewModel: GameViewModel
) {
    val uiState by gameViewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.save_zone),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.2f
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(OverlayDark.copy(alpha = 0.6f))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CounterPill(iconRes = R.drawable.item_egg, value = uiState.stats.eggs)
            Image(
                painter = painterResource(id = R.drawable.chicken_happy),
                contentDescription = null,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .size(220.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = "Blocked Tunnel!",
                color = TextLight,
                style = MaterialTheme.typography.displayLarge,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Column(
                modifier = Modifier.padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PrimaryButton(text = "Try again", onClick = onRetry, modifier = Modifier.fillMaxWidth())
                PrimaryButton(text = "Lobby", onClick = onLobby, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
