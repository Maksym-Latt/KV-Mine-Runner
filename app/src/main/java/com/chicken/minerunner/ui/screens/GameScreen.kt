package com.chicken.minerunner.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.chicken.dropper.ui.components.PrimaryButton
import com.chicken.dropper.ui.components.SecondaryButton
import com.chicken.minerunner.R
import com.chicken.minerunner.domain.config.GameConfig
import com.chicken.minerunner.domain.model.GameStatus
import com.chicken.minerunner.domain.model.GameUiState
import com.chicken.minerunner.domain.model.ItemType
import com.chicken.minerunner.domain.model.LaneType
import com.chicken.minerunner.domain.model.SwipeDirection
import com.chicken.minerunner.ui.components.EggCounter
import com.chicken.minerunner.ui.sound.SoundManager
import com.chicken.minerunner.ui.theme.CopperDark
import com.chicken.minerunner.ui.theme.OverlayBlue
import kotlin.math.abs

private val laneHeightDp = GameConfig.laneHeight.dp

@Composable
fun GameScreen(
    state: GameUiState,
    onSwipe: (SwipeDirection) -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onExit: () -> Unit,
    musicEnabled: Boolean,
    sfxEnabled: Boolean,
    soundManager: SoundManager,
    onGameOver: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> onPause()
                Lifecycle.Event.ON_RESUME -> onResume()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(state.status) {
        if (state.status is GameStatus.GameOver) onGameOver()
    }

    LaunchedEffect(musicEnabled, state.status) {
        if (state.status is GameStatus.GameOver) return@LaunchedEffect
        soundManager.playGameMusic(musicEnabled)
    }

    var lastEggs by remember { mutableIntStateOf(state.stats.eggs) }
    var lastLives by remember { mutableIntStateOf(state.stats.lives) }
    var magnetActive by remember { mutableStateOf(state.stats.magnetActiveMs > 0) }
    var helmetActive by remember { mutableStateOf(state.stats.helmetActiveMs > 0) }

    LaunchedEffect(state.stats.eggs) {
        if (state.stats.eggs > lastEggs) {
            soundManager.playSfx(R.raw.sfx_egg, sfxEnabled)
        }
        lastEggs = state.stats.eggs
    }

    LaunchedEffect(state.stats.lives) {
        if (state.stats.lives > lastLives) {
            soundManager.playSfx(R.raw.sfx_bonus, sfxEnabled)
        }
        lastLives = state.stats.lives
    }

    LaunchedEffect(state.stats.magnetActiveMs, state.stats.helmetActiveMs) {
        val newMagnet = state.stats.magnetActiveMs > 0
        val newHelmet = state.stats.helmetActiveMs > 0
        if ((newMagnet && !magnetActive) || (newHelmet && !helmetActive)) {
            soundManager.playSfx(R.raw.sfx_bonus, sfxEnabled)
        }
        magnetActive = newMagnet
        helmetActive = newHelmet
    }

    LaunchedEffect(state.status) {
        if (state.status is GameStatus.GameOver) {
            soundManager.playSfx(R.raw.sfx_lose, sfxEnabled)
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(CopperDark)
            .pointerInput(state.status) {
                detectDragGestures(onDragEnd = {}) { change, drag ->
                    change.consume()
                    val (dx, dy) = drag
                    if (abs(dx) > abs(dy)) {
                        if (dx > 40) onSwipe(SwipeDirection.Right) else if (dx < -40) onSwipe(SwipeDirection.Left)
                    } else {
                        if (dy < -40) onSwipe(SwipeDirection.Forward)
                    }
                }
            }
    ) {
        val density = LocalDensity.current
        val laneHeightPx = with(density) { laneHeightDp.toPx() }
        val widthPx = constraints.maxWidth.toFloat()
        val heightPx = constraints.maxHeight.toFloat()

        Box(modifier = Modifier.fillMaxSize()) {
            state.segments.forEach { segment ->
                val y = heightPx - (segment.index * laneHeightPx - state.cameraOffset)
                if (y < -laneHeightPx || y > heightPx + laneHeightPx) return@forEach
                val painter = when (segment.type) {
                    LaneType.SafeZone -> painterResource(id = R.drawable.save_zone)
                    LaneType.Railway -> painterResource(id = R.drawable.railway)
                }
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .width(with(density) { widthPx.toDp() })
                        .height(laneHeightDp)
                        .align(Alignment.TopStart)
                        .offset(y = with(density) { (y - laneHeightPx).toDp() })
                )

                val columnWidth = widthPx / 3f
                val centerX = widthPx / 2f

                segment.items.forEach { item ->
                    val itemPainter = when (item.type) {
                        ItemType.Egg -> painterResource(id = R.drawable.item_egg)
                        ItemType.Magnet -> painterResource(id = R.drawable.item_magnet)
                        ItemType.Helmet -> painterResource(id = R.drawable.item_helmet)
                        ItemType.ExtraLife -> painterResource(id = R.drawable.item_extra_life)
                    }
                    val x = centerX + item.column * columnWidth / 1.2f
                    Image(
                        painter = itemPainter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(56.dp)
                            .align(Alignment.TopStart)
                            .offset(
                                x = with(density) { (x - 28f).toDp() },
                                y = with(density) { (y - laneHeightPx / 2).toDp() }
                            )
                    )
                }

                segment.trolley?.let { trolley ->
                    val trackHalfWidth = widthPx / 2f - 70f
                    val trolleyFraction = trolley.position / GameConfig.trolleyBounds
                    val x = centerX + trolleyFraction * trackHalfWidth
                    Image(
                        painter = painterResource(id = R.drawable.trolley),
                        contentDescription = null,
                        modifier = Modifier
                            .size(110.dp)
                            .align(Alignment.TopStart)
                            .offset(
                                x = with(density) { (x - 55f).toDp() },
                                y = with(density) { (y - laneHeightPx / 2.4f).toDp() }
                            )
                    )
                }
            }

            val columnWidth = widthPx / 3f
            val centerX = widthPx / 2f
            val chickenX = centerX + state.chickenColumn * columnWidth / 1.2f
            Image(
                painter = painterResource(id = R.drawable.chicken_run),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.Center)
                    .offset(
                        x = with(density) { (chickenX - 60f).toDp() },
                        y = 12.dp
                    )
            )
        }

        TopPanel(state, onPause, onExit)

        if (state.status is GameStatus.Paused) {
            PauseOverlay(onResume = onResume, onExit = onExit)
        }
    }
}

@Composable
private fun TopPanel(state: GameUiState, onPause: () -> Unit, onExit: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SecondaryButton(
                icon = rememberVectorPainter(image = Icons.Default.Home),
                onClick = onExit
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Distance ${state.stats.distance}m", color = Color.White, fontSize = 16.sp)
                EggCounter(
                    count = state.stats.eggs,
                    eggIcon = R.drawable.item_egg,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                repeat(state.stats.lives) {
                    Image(
                        painter = painterResource(id = R.drawable.item_extra_life),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                SecondaryButton(
                    icon = rememberVectorPainter(image = Icons.Default.Pause),
                    onClick = onPause
                )
            }
        }
    }
}

@Composable
private fun PauseOverlay(onResume: () -> Unit, onExit: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = OverlayBlue.copy(alpha = 0.6f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Paused",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))
            PrimaryButton(text = "Resume", onClick = onResume)
            Spacer(modifier = Modifier.height(8.dp))
            PrimaryButton(text = "Main menu", style = com.chicken.dropper.ui.components.ChickenButtonStyle.Blue, onClick = onExit)
        }
    }
}
