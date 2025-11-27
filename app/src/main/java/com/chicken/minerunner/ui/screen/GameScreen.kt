package com.chicken.minerunner.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.width
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.chicken.minerunner.R
import com.chicken.minerunner.core.domain.model.SwipeDirection
import com.chicken.minerunner.core.domain.model.TrackType
import com.chicken.minerunner.ui.components.BadgeCounter
import com.chicken.minerunner.ui.components.GradientButton
import com.chicken.minerunner.ui.game.GameViewModel
import com.chicken.minerunner.ui.theme.MineShadow
import kotlin.math.abs

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onGameOver: () -> Unit,
    onBackToMenu: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) viewModel.pause()
            if (event == Lifecycle.Event.ON_RESUME) viewModel.resume()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(state.gameOver) {
        if (state.gameOver) onGameOver()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MineShadow)
            .pointerInput(Unit) {
                detectDragGestures(onDragEnd = {}) { _, dragAmount ->
                    val direction = if (abs(dragAmount.x) > abs(dragAmount.y)) {
                        if (dragAmount.x > 0) SwipeDirection.RIGHT else SwipeDirection.LEFT
                    } else {
                        if (dragAmount.y < 0) SwipeDirection.FORWARD else null
                    }
                    direction?.let { viewModel.swipe(it) }
                }
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.railway),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.4f
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            HudRow(state.distance, state.lives, state.eggs, state.magnetActive, state.helmetActive)
            TrackView(state.rows.takeLast(7), state.currentRowIndex, state.lane)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GradientButton(text = "PAUSE", modifier = Modifier.weight(1f)) { viewModel.pause() }
                Spacer(modifier = Modifier.width(12.dp))
                GradientButton(text = "LOBBY", modifier = Modifier.weight(1f)) { onBackToMenu() }
            }
        }
        if (state.isPaused && !state.gameOver) {
            PauseOverlay(onResume = { viewModel.resume() })
        }
    }
}

@Composable
private fun HudRow(distance: Int, lives: Int, eggs: Int, magnet: Boolean, helmet: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BadgeCounter(iconRes = R.drawable.item_egg, value = eggs.toString())
        Text(
            text = "${distance}m",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
        BadgeCounter(iconRes = R.drawable.item_extra_life, value = lives.toString())
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        if (magnet) BadgeCounter(iconRes = R.drawable.item_magnet, value = "MAGNET")
        if (helmet) BadgeCounter(iconRes = R.drawable.item_helmet, value = "HELMET")
    }
}

@Composable
private fun TrackView(rows: List<com.chicken.minerunner.core.domain.model.TrackRow>, currentRow: Int, lane: com.chicken.minerunner.core.domain.model.LanePosition) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        rows.forEach { row ->
            val isCurrent = row.id == currentRow
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (row.type == TrackType.SAFE_ZONE) Color(0xFF22402F) else Color(0xFF3A2F1B),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    row.lanes.forEach { laneContent ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (laneContent.hasTrolley && row.type == TrackType.RAILWAY) {
                                Image(painter = painterResource(id = R.drawable.trolley), contentDescription = null)
                            }
                            laneContent.item?.let { item ->
                                val icon = when (item) {
                                    com.chicken.minerunner.core.domain.model.ItemType.EGG -> R.drawable.item_egg
                                    com.chicken.minerunner.core.domain.model.ItemType.HELMET -> R.drawable.item_helmet
                                    com.chicken.minerunner.core.domain.model.ItemType.MAGNET -> R.drawable.item_magnet
                                    com.chicken.minerunner.core.domain.model.ItemType.EXTRA_LIFE -> R.drawable.item_extra_life
                                }
                                Image(painter = painterResource(id = icon), contentDescription = null)
                            }
                            if (isCurrent && laneContent.lane == lane) {
                                Image(painter = painterResource(id = R.drawable.chicken_run), contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PauseOverlay(onResume: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Text(text = "PAUSED", style = MaterialTheme.typography.displayLarge, color = Color.White, fontWeight = FontWeight.Bold)
            GradientButton(text = "RESUME", modifier = Modifier.fillMaxWidth(0.7f)) { onResume() }
        }
    }
}
