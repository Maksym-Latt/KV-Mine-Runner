package com.chicken.minerunner.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.matchParentSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.minerunner.R
import com.chicken.minerunner.presentation.ui.components.CounterPill
import com.chicken.minerunner.presentation.ui.components.PrimaryButton
import com.chicken.minerunner.presentation.viewmodel.GameViewModel
import com.chicken.minerunner.ui.theme.OverlayDark
import com.chicken.minerunner.ui.theme.TextLight

@Composable
fun GameScreen(
    onPause: () -> Unit,
    onFinish: () -> Unit,
    onUpgrade: () -> Unit,
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
                .background(OverlayDark.copy(alpha = 0.4f))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    CounterPill(iconRes = R.drawable.item_egg, value = uiState.stats.eggs)
                    CounterPill(iconRes = R.drawable.item_magnet, value = uiState.stats.coins)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    LivesRow(lives = uiState.stats.lives)
                    IconButton(onClick = onUpgrade) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Upgrade",
                            tint = TextLight
                        )
                    }
                    IconButton(onClick = onPause) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_media_pause),
                            contentDescription = "Pause",
                            tint = TextLight
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Distance ${uiState.stats.distance}m",
                color = TextLight,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TrackRow()
                TrackRow(showEgg = true)
                TrackRow(showTrolley = true)
                TrackRow()
            }

            Spacer(modifier = Modifier.height(24.dp))
            PrimaryActionRow(onPause = onPause, onFinish = onFinish)
        }
    }
}

@Composable
private fun TrackRow(showEgg: Boolean = false, showTrolley: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) { index ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 6.dp)
                    .height(96.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.railway),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .matchParentSize()
                        .alpha(0.9f)
                )
                if (showEgg && index == 1) {
                    Image(
                        painter = painterResource(id = R.drawable.item_egg),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                }
                if (showTrolley && index == 2) {
                    Image(
                        painter = painterResource(id = R.drawable.trolley),
                        contentDescription = null,
                        modifier = Modifier.size(64.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun LivesRow(lives: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        repeat(4) { index ->
            val active = index < lives
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .background(
                        color = if (active) Color.Red else Color.LightGray,
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
private fun PrimaryActionRow(onPause: () -> Unit, onFinish: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PrimaryButton(text = "Pause", onClick = onPause, modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(12.dp))
        PrimaryButton(text = "Game Over", onClick = onFinish, modifier = Modifier.weight(1f))
    }
}
