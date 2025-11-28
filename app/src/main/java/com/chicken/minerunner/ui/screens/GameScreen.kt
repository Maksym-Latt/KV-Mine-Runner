package com.chicken.minerunner.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.chicken.dropper.ui.components.ActionButton
import com.chicken.dropper.ui.components.ChickenButtonStyleVariant
import com.chicken.dropper.ui.components.IconAccentButton
import com.chicken.minerunner.R
import com.chicken.minerunner.domain.config.GameConfig
import com.chicken.minerunner.domain.model.GameStatus
import com.chicken.minerunner.domain.model.GameUiState
import com.chicken.minerunner.domain.model.ItemType
import com.chicken.minerunner.domain.model.LaneType
import com.chicken.minerunner.domain.model.SwipeDirection
import com.chicken.minerunner.ui.components.GradientText
import com.chicken.minerunner.ui.components.EggCounterBox
import com.chicken.minerunner.sound.SoundManager
import com.chicken.minerunner.ui.theme.CopperDark


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun GameScreen(
    state: GameUiState,
    onSwipe: (SwipeDirection) -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onExit: () -> Unit,
    onRetry: () -> Unit,
    musicEnabled: Boolean,
    sfxEnabled: Boolean,
    soundManager: SoundManager,
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

    LaunchedEffect(musicEnabled, state.status) {
        if (state.status !is GameStatus.GameOver) soundManager.playGameMusic(musicEnabled)
    }

    var lastEggs by remember { mutableIntStateOf(state.stats.eggs) }
    var lastLives by remember { mutableIntStateOf(state.stats.lives) }

    var lastMagnetMs by remember { mutableStateOf(state.stats.magnetActiveMs) }
    var lastHelmetMs by remember { mutableStateOf(state.stats.helmetActiveMs) }

    LaunchedEffect(state.stats.eggs) {
        if (state.stats.eggs > lastEggs) soundManager.playSfx(R.raw.sfx_egg, sfxEnabled)
        lastEggs = state.stats.eggs
    }
    LaunchedEffect(state.stats.lives) {
        if (state.stats.lives > lastLives) soundManager.playSfx(R.raw.sfx_bonus, sfxEnabled)
        lastLives = state.stats.lives
    }
    LaunchedEffect(state.stats.magnetActiveMs) {
        val current = state.stats.magnetActiveMs
        if (current > lastMagnetMs) {
            soundManager.playSfx(R.raw.sfx_bonus, sfxEnabled)
        }
        lastMagnetMs = current
    }
    LaunchedEffect(state.stats.helmetActiveMs) {
        val current = state.stats.helmetActiveMs
        if (current > lastHelmetMs) {
            soundManager.playSfx(R.raw.sfx_bonus, sfxEnabled)
        }
        lastHelmetMs = current
    }
    LaunchedEffect(state.status) {
        if (state.status is GameStatus.GameOver) soundManager.playSfx(R.raw.sfx_lose, sfxEnabled)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        var showIntro by remember { mutableStateOf(true) }
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(CopperDark)
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val down = awaitFirstDown()

                            val startX = down.position.x
                            val startY = down.position.y

                            var endX = startX
                            var endY = startY

                            var pointer = down.id

                            while (true) {
                                val event = awaitPointerEvent()

                                val change = event.changes.firstOrNull { it.id == pointer }
                                if (change == null) break

                                if (change.positionChanged()) {
                                    endX = change.position.x
                                    endY = change.position.y
                                }

                                if (change.changedToUp()) {
                                    break
                                }
                            }

                            val dx = endX - startX
                            val dy = endY - startY

                            val ax = kotlin.math.abs(dx)
                            val ay = kotlin.math.abs(dy)

                            val threshold = 50f

                            if (ax < threshold && ay < threshold) {
                                continue
                            }

                            if (ax > ay) {
                                if (dx > 0) {
                                    if (state.status is GameStatus.Running) {
                                        soundManager.playSfx(R.raw.sfx_jump, sfxEnabled)
                                    }
                                    onSwipe(SwipeDirection.Right)
                                } else {
                                    if (state.status is GameStatus.Running) {
                                        soundManager.playSfx(R.raw.sfx_jump, sfxEnabled)
                                    }
                                    onSwipe(SwipeDirection.Left)
                                }
                            } else {
                                if (dy < 0) {
                                    if (state.status is GameStatus.Running) {
                                        soundManager.playSfx(R.raw.sfx_jump, sfxEnabled)
                                    }
                                    onSwipe(SwipeDirection.Forward)
                                }
                            }

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
            val laneHeights = mutableMapOf<Int, Pair<Float, Float>>()
            val laneWidth = w / GameConfig.trackCount

            state.segments.forEach { seg ->
                val lh = if (seg.type == LaneType.SafeZone) safeH else railH
                val lhd = with(density) { lh.toDp() }
                val y = h - acc - lh + state.cameraOffset
                acc += lh
                if (y < -lh || y > h) return@forEach

                laneHeights[seg.index] = y to lh

                val p: Painter =
                    if (seg.type == LaneType.SafeZone)
                        painterResource(R.drawable.save_zone)
                    else
                        painterResource(R.drawable.railway)

                val isFlipped = seg.type == LaneType.SafeZone && seg.flipped

                Image(
                    painter = p,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(lhd)
                        .offset(y = with(density) { y.toDp() })
                        .graphicsLayer {
                            rotationZ = if (isFlipped) 180f else 0f
                            scaleX = if (isFlipped) -1f else 1f
                        },
                    contentScale = ContentScale.FillBounds
                )


                seg.items.filter { it.active }.forEach {
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

            val lane = state.segments.firstOrNull { it.index == state.chickenLane }
            val lanePosition = laneHeights[state.chickenLane]
            if (lane != null && lanePosition != null) {
                val (laneY, lh) = lanePosition
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
            PauseOverlay(onResume = onResume, onRestart = onExit, onExit = onExit)
        }

        if (showIntro) {
            GameIntroOverlay(onStart = { showIntro = false })
        }

        if (state.status is GameStatus.GameOver) {
            GameOverScreen(
                reward = state.stats.eggs,
                onRetry = onRetry,
                onLobby = onExit
            )
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
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconAccentButton(
            iconPainter = rememberVectorPainter(Icons.Default.Pause),
            onPress = onPause
        )

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.wrapContentWidth()
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EggCounterBox(
                    amount = state.stats.eggs,
                    iconRes = R.drawable.item_egg
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Distance: ${state.stats.distance}m",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.shadow(4.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            AbilityTimerBar(
                label = "Magnet",
                remainingMs = state.stats.magnetActiveMs,
                totalMs = state.stats.magnetDurationMs,
                color = Color(0xFF5AA2FF)
            )

            AbilityTimerBar(
                label = "Helmet",
                remainingMs = state.stats.helmetActiveMs,
                totalMs = state.stats.helmetDurationMs,
                color = Color(0xFFFFC857),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(state.stats.lives) {
                    Image(
                        painter = painterResource(id = R.drawable.item_extra_life),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AbilityTimerBar(
    label: String,
    remainingMs: Long,
    totalMs: Long,
    color: Color,
    modifier: Modifier = Modifier
) {
    if (remainingMs <= 0 || totalMs <= 0) return
    val progress = (remainingMs.toFloat() / totalMs).coerceIn(0f, 1f)
    val secondsLeft = remainingMs / 1000f

    Column(modifier = modifier) {
        Text(
            text = "$label: ${"%.1f".format(secondsLeft)}s",
            color = Color.White,
            fontSize = 12.sp
        )
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .padding(top = 2.dp)
                .width(140.dp)
                .height(10.dp)
                .clip(RoundedCornerShape(12.dp)),
            color = color,
            trackColor = Color.White.copy(alpha = 0.25f)
        )
    }
}
