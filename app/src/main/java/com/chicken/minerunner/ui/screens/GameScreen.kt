package com.chicken.minerunner.ui.screens

import android.R.attr.y
import android.annotation.SuppressLint
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
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
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


@SuppressLint("UnusedBoxWithConstraintsScope")
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
        val observer = LifecycleEventObserver { _, e ->
            if (e == Lifecycle.Event.ON_STOP) onPause()
            if (e == Lifecycle.Event.ON_RESUME) onResume()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(state.status) {
        if (state.status is GameStatus.GameOver) onGameOver()
    }

    LaunchedEffect(musicEnabled, state.status) {
        if (state.status !is GameStatus.GameOver) soundManager.playGameMusic(musicEnabled)
    }

    var lastEggs by remember { mutableIntStateOf(state.stats.eggs) }
    var lastLives by remember { mutableIntStateOf(state.stats.lives) }
    var magnetActive by remember { mutableStateOf(state.stats.magnetActiveMs > 0) }
    var helmetActive by remember { mutableStateOf(state.stats.helmetActiveMs > 0) }

    LaunchedEffect(state.stats.eggs) {
        if (state.stats.eggs > lastEggs) soundManager.playSfx(R.raw.sfx_egg, sfxEnabled)
        lastEggs = state.stats.eggs
    }
    LaunchedEffect(state.stats.lives) {
        if (state.stats.lives > lastLives) soundManager.playSfx(R.raw.sfx_bonus, sfxEnabled)
        lastLives = state.stats.lives
    }
    LaunchedEffect(state.stats.magnetActiveMs, state.stats.helmetActiveMs) {
        val m = state.stats.magnetActiveMs > 0
        val h = state.stats.helmetActiveMs > 0
        if (!magnetActive && m) soundManager.playSfx(R.raw.sfx_bonus, sfxEnabled)
        if (!helmetActive && h) soundManager.playSfx(R.raw.sfx_bonus, sfxEnabled)
        magnetActive = m
        helmetActive = h
    }
    LaunchedEffect(state.status) {
        if (state.status is GameStatus.GameOver) soundManager.playSfx(R.raw.sfx_lose, sfxEnabled)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(CopperDark)
                .pointerInput(state.status) {
                    detectDragGestures { change, drag ->
                        change.consume()
                        val (dx, dy) = drag
                        val ax = abs(dx)
                        val ay = abs(dy)
                        if (ax > ay && ax > 50) {
                            if (dx > 0) onSwipe(SwipeDirection.Right)
                            else onSwipe(SwipeDirection.Left)
                        } else if (ay > 50 && dy < 0) {
                            onSwipe(SwipeDirection.Forward)
                        }
                    }
                }
        ) {
            val density = LocalDensity.current
            val w = constraints.maxWidth.toFloat()
            val h = constraints.maxHeight.toFloat()

            val safeBmp = ImageBitmap.imageResource(R.drawable.save_zone)
            val railBmp = ImageBitmap.imageResource(R.drawable.railway)

            val safeAspect = safeBmp.width.toFloat() / safeBmp.height
            val railAspect = railBmp.width.toFloat() / railBmp.height

            val safeH = w / safeAspect
            val railH = w / railAspect

            GameConfig.safeZoneHeightPx = safeH
            GameConfig.railwayHeightPx = railH

            var acc = 0f

            state.segments.forEach { seg ->
                val lh = if (seg.type == LaneType.SafeZone) safeH else railH
                val lhd = with(density) { lh.toDp() }
                val y = h - acc - lh + state.cameraOffset
                acc += lh
                if (y < -lh || y > h) return@forEach

                val p: Painter =
                    if (seg.type == LaneType.SafeZone) painterResource(R.drawable.save_zone)
                    else painterResource(R.drawable.railway)

                Image(
                    painter = p,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(lhd)
                        .offset(y = with(density) { y.toDp() }),
                    contentScale = ContentScale.FillBounds
                )

                val laneWidth = w / GameConfig.trackCount

                seg.items.forEach {
                    val ip = when (it.type) {
                        ItemType.Egg -> painterResource(R.drawable.item_egg)
                        ItemType.Magnet -> painterResource(R.drawable.item_magnet)
                        ItemType.Helmet -> painterResource(R.drawable.item_helmet)
                        ItemType.ExtraLife -> painterResource(R.drawable.item_extra_life)
                    }

                    val laneIndex = GameConfig.columns.indexOf(it.column)
                    val ix = laneWidth * laneIndex + laneWidth / 2f

                    Image(
                        painter = ip,
                        contentDescription = null,
                        modifier = Modifier
                            .size(56.dp)
                            .offset(
                                x = with(density) { (ix - 28).toDp() },
                                y = with(density) { (y + lh / 2 - 28).toDp() }
                            )
                    )
                }

                seg.trolley?.let { tr ->
                    val tw = with(density) { 110.dp.toPx() }
                    val th = with(density) { 110.dp.toPx() }
                    val cx = w / 2f
                    val cy = y + lh / 2f
                    val track = w / 2 + tw
                    val fx = tr.position / GameConfig.trolleyBounds
                    val tx = cx + fx * track
                    Image(
                        painter = painterResource(R.drawable.trolley),
                        contentDescription = null,
                        modifier = Modifier
                            .size(110.dp)
                            .offset(
                                x = with(density) { (tx - tw / 2).toDp() },
                                y = with(density) { (cy - th / 2).toDp() }
                            )
                    )
                }
            }

            val laneWidth = w / GameConfig.trackCount

            val lane = state.segments.firstOrNull { it.index == state.chickenLane }
            if (lane != null) {
                val lh = if (lane.type == LaneType.SafeZone) safeH else railH
                val laneY = h - lh * (lane.index + 1) + state.cameraOffset
                val cy = laneY + lh / 2

                val laneIndex = GameConfig.columns.indexOf(state.chickenColumn)
                val cx = laneWidth * laneIndex + laneWidth / 2f

                val cwp = with(density) { 120.dp.toPx() }

                Image(
                    painter = painterResource(R.drawable.chicken_run),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .offset(
                            x = with(density) { (cx - cwp / 2).toDp() },
                            y = with(density) { (cy - cwp / 2).toDp() }
                        )
                )
            }
        }

        TopPanel(
            state = state,
            onPause = onPause,
            onExit = onExit,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 12.dp)
        )

        if (state.status is GameStatus.Paused) {
            PauseOverlay(onResume = onResume, onExit = onExit)
        }
    }
}

@Composable
private fun TopPanel(
    state: GameUiState,
    onPause: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SecondaryButton(
            icon = rememberVectorPainter(Icons.Default.Home),
            onClick = onExit
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Distance ${state.stats.distance}m",
                color = Color.White,
                fontSize = 16.sp
            )
            EggCounter(
                count = state.stats.eggs,
                eggIcon = R.drawable.item_egg,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(state.stats.lives) {
                Image(
                    painter = painterResource(id = R.drawable.item_extra_life),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }

            SecondaryButton(
                icon = rememberVectorPainter(Icons.Default.Person),
                onClick = onPause
            )
        }
    }
}
