package com.chicken.minerunner.ui.theme.gamescreen

import android.app.Activity
import android.view.ViewTreeObserver
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.chicken.roadchicken.R
import com.chicken.roadchicken.domain.model.gamemodels.Car
import com.chicken.roadchicken.domain.model.gamemodels.Chicken
import com.chicken.roadchicken.domain.model.gamemodels.Coin
import com.chicken.roadchicken.ui.component.ControlButton
import com.chicken.roadchicken.ui.component.RoundBtnWithIcon
import com.chicken.roadchicken.ui.component.ScorePanel
import kotlinx.coroutines.delay
import java.util.Timer
import java.util.TimerTask
import kotlin.apply
import kotlin.collections.all
import kotlin.collections.filterNot
import kotlin.collections.forEach
import kotlin.collections.indices
import kotlin.collections.map
import kotlin.collections.mapIndexed
import kotlin.collections.random
import kotlin.collections.shuffled
import kotlin.collections.take
import kotlin.let
import kotlin.random.Random
import kotlin.ranges.coerceIn
import kotlin.ranges.until
import kotlin.run

// ------------------------------------------------------------
// 🔧 Налаштування за замовчуванням (можеш змінити будь-коли)
// ------------------------------------------------------------
const val DEFAULT_PLAYER_SPEED_PX_PER_TICK = 10f
const val DEFAULT_PLAYER_COLLIDER_SHRINK_X = 0.5f
const val DEFAULT_PLAYER_COLLIDER_SHRINK_Y = 0.6f
const val DEFAULT_CAR_COLLIDER_SHRINK  = 0.75f

// ------------------------------------------------------------
// 🔧 Хелпери для колізій
// ------------------------------------------------------------
private data class Bounds(val left: Float, val top: Float, val right: Float, val bottom: Float) {
    val width: Float get() = right - left
    val height: Float get() = bottom - top
}

private fun shrunkBounds(x: Float, y: Float, w: Float, h: Float, shrinkX: Float, shrinkY: Float): Bounds {
    val fx = shrinkX.coerceIn(0.1f, 1.2f)
    val fy = shrinkY.coerceIn(0.1f, 1.2f)
    val dw = w * (1f - fx)
    val dh = h * (1f - fy)
    val left = x + dw / 2f
    val top = y + dh / 2f
    return Bounds(left, top, left + w * fx, top + h * fy)
}

private fun intersects(a: Bounds, b: Bounds): Boolean =
    a.left < b.right && a.right > b.left && a.top < b.bottom && a.bottom > b.top

@Composable
fun GamePlayScreen(
    onGameOver: (score: Int) -> Unit,
    onCoinCollected: () -> Unit,
    onCarCollision: () -> Unit,
    onRestartRequest: () -> Unit,
    onMenuRequest: () -> Unit,
    restartKey: Int,
    musicEnabled: Boolean,
    soundEnabled: Boolean,
    vibrationEnabled: Boolean,
    onToggleMusic: () -> Unit,
    onToggleSound: () -> Unit,
    onToggleVibration: () -> Unit,
    showColliders: Boolean = false,

    playerSpeedPxPerTick: Float = DEFAULT_PLAYER_SPEED_PX_PER_TICK,
    playerColliderShrinkX: Float = DEFAULT_PLAYER_COLLIDER_SHRINK_X,
    playerColliderShrinkY: Float = DEFAULT_PLAYER_COLLIDER_SHRINK_Y,
    carColliderShrink: Float = DEFAULT_CAR_COLLIDER_SHRINK
) {
    // 🎨 Resources
    val spawnImage = ImageBitmap.imageResource(R.drawable.spawn)
    val spawnDecoration = ImageBitmap.imageResource(R.drawable.spawn_decoration)
    val roadImage = ImageBitmap.imageResource(R.drawable.road)
    val chickenNormal = ImageBitmap.imageResource(R.drawable.player)
    val chickenLose = ImageBitmap.imageResource(R.drawable.player_lose)
    val coinImage = ImageBitmap.imageResource(R.drawable.ic_coin)
    val carTextures = listOf(
        ImageBitmap.imageResource(R.drawable.car_1),
        ImageBitmap.imageResource(R.drawable.car_2),
        ImageBitmap.imageResource(R.drawable.car_3)
    )

    // State
    val chicken by remember(restartKey) { mutableStateOf(Chicken(0f, 0f)) }
    var cars by remember(restartKey) { mutableStateOf(listOf<Car>()) }
    var coins by remember(restartKey) { mutableStateOf(listOf<Coin>()) }
    var score by remember(restartKey) { mutableStateOf(0) }
    var coinCounter by remember(restartKey) { mutableStateOf(0) }

    var isPaused by remember(restartKey) { mutableStateOf(false) }
    var isGameOver by remember(restartKey) { mutableStateOf(false) }

    var chickenDir by remember(restartKey) { mutableStateOf(Pair(0f, 0f)) }
    var chickenImageState by remember(restartKey) { mutableStateOf(chickenNormal) }

    var localMusicEnabled by remember { mutableStateOf(musicEnabled) }
    var localSoundEnabled by remember { mutableStateOf(soundEnabled) }
    var localVibrationEnabled by remember { mutableStateOf(vibrationEnabled) }

    LaunchedEffect(musicEnabled, soundEnabled, vibrationEnabled) {
        localMusicEnabled = musicEnabled
        localSoundEnabled = soundEnabled
        localVibrationEnabled = vibrationEnabled
    }

    //=======================================================================
    val lifecycleOwner = LocalLifecycleOwner.current
    val view = LocalView.current
    val context = LocalContext.current

    // Обробка кнопки "назад"
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    // Єдиний вхід у паузу
    val triggerPause: () -> Unit = remember {
        {
            if (!isGameOver) {
                isPaused = true
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE,
                Lifecycle.Event.ON_STOP -> {
                    if (!isPaused && !isGameOver) triggerPause()
                }
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    // Логіка паузи по втраті фокуса вікна (комбінований підхід)
    DisposableEffect(view) {
        var pauseTimeout: Timer? = null
        var isVolumeOverlayVisible = false

        val focusListener = ViewTreeObserver.OnWindowFocusChangeListener { hasFocus ->
            if (!hasFocus && !isPaused && !isGameOver) {
                val rootView = view.rootView
                val decorView = (context as? Activity)?.window?.decorView

                pauseTimeout?.cancel()
                pauseTimeout = Timer().apply {
                    schedule(object : TimerTask() {
                        override fun run() {
                            val layoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
                                override fun onGlobalLayout() {
                                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                                    if (!view.hasWindowFocus() && !isPaused) {
                                        triggerPause()
                                    }
                                }
                            }
                            view.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)
                        }
                    }, 800)
                }
            } else if (hasFocus) {
                pauseTimeout?.cancel()
            }
        }

        view.viewTreeObserver.addOnWindowFocusChangeListener(focusListener)
        onDispose {
            view.viewTreeObserver.removeOnWindowFocusChangeListener(focusListener)
            pauseTimeout?.cancel()
        }
    }

    //Обробка кнопки "назад"
    DisposableEffect(backPressedDispatcher) {
        val callback = backPressedDispatcher?.let { dispatcher ->
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    when {
                        isGameOver -> {
                            isEnabled = false
                            dispatcher.onBackPressed()
                        }
                        !isPaused -> {
                            isPaused = true
                        }
                        else -> {
                            (context as? Activity)?.moveTaskToBack(true)
                        }
                    }
                }
            }
        }

        callback?.let { backPressedDispatcher?.addCallback(it) }

        onDispose {
            callback?.remove()
        }
    }
    //==================================================================


    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val baseScale = screenWidthDp.value / 411f
    val density = LocalDensity.current

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenWidthPx = with(density) { maxWidth.toPx() }
        val screenHeightPx = with(density) { maxHeight.toPx() }
        val chickenSizePx = with(density) { (60.dp).toPx() }
        val coinSizePx = with(density) { (30 * baseScale).dp.toPx() }

        val spawnAspect = spawnImage.width.toFloat() / spawnImage.height.toFloat()
        val spawnHeightPx = screenWidthPx / spawnAspect

        val roadAspect = roadImage.width.toFloat() / roadImage.height.toFloat()
        val roadHeightPx = screenWidthPx / roadAspect
        val roadCount = ((screenHeightPx - spawnHeightPx) / roadHeightPx).toInt()
        val roadPositions = (0 until roadCount).map { it * roadHeightPx }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .matchParentSize()
                .background(Color(0xff70716b))
        )

        // Cars generation
        LaunchedEffect(restartKey) {
            cars = roadPositions.mapIndexed { i, y ->
                val laneCenterY = y + roadHeightPx / 2
                Car(
                    id = i,
                    x = Random.nextFloat() * screenWidthPx,
                    y = laneCenterY,
                    speed = Random.nextInt(3, 11) * if (i % 2 == 0) 1f else -1f,
                    texture = carTextures.random(),
                    widthPx = roadHeightPx * 1.2f,
                    heightPx = roadHeightPx * 1f
                )
            }
        }

        // Coins generation
        LaunchedEffect(restartKey) {
            coins = roadPositions.indices.shuffled().take(3).map { lane ->
                Coin(
                    id = coinCounter++,
                    x = Random.nextFloat() * (screenWidthPx - coinSizePx),
                    y = roadPositions[lane] + roadHeightPx / 2,
                    isCollected = false
                )
            }
        }

        // SpawnPoint
        LaunchedEffect(restartKey) {
            chicken.x = screenWidthPx / 2 - chickenSizePx / 2
            chicken.y = screenHeightPx - spawnHeightPx / 2 - chickenSizePx / 2
            chickenImageState = chickenNormal
        }

        // Game loop
        LaunchedEffect(restartKey) {
            while (true) {
                if (!isPaused) {
                    chicken.x = (chicken.x + chickenDir.first * playerSpeedPxPerTick)
                        .coerceIn(0f, screenWidthPx - chickenSizePx)
                    chicken.y = (chicken.y + chickenDir.second * playerSpeedPxPerTick)
                        .coerceIn(0f, screenHeightPx - chickenSizePx)

                    cars = cars.map { car ->
                        var newX = car.x + car.speed
                        if (car.speed > 0 && newX > screenWidthPx) newX = -car.widthPx
                        if (car.speed < 0 && newX < -car.widthPx) newX = screenWidthPx
                        car.copy(x = newX)
                    }

                    // Check collision
                    for (car in cars) {
                        if (checkCollisionSimple(
                                chicken, chickenSizePx, car,
                                playerShrinkX = playerColliderShrinkX,
                                playerShrinkY = playerColliderShrinkY,
                                carShrink = carColliderShrink
                            )
                        ) {
                            onCarCollision()
                            chickenImageState = chickenLose
                            isGameOver = true
                            isPaused = false
                            delay(600L)
                            onGameOver(score)
                            return@LaunchedEffect
                        }
                    }

                    // coins
                    coins = coins.map { coin ->
                        if (!coin.isCollected && checkCoinPickup(
                                chicken, chickenSizePx, coin, coinSizePx
                            )
                        ) {
                            onCoinCollected()
                            score += 10
                            coin.copy(isCollected = true)
                        } else coin
                    }

                    if (coins.all { it.isCollected }) {
                        coins = roadPositions.indices.shuffled().take(3).map { lane ->
                            Coin(
                                id = coinCounter++,
                                x = Random.nextFloat() * (screenWidthPx - coinSizePx),
                                y = roadPositions[lane] + roadHeightPx / 2,
                                isCollected = false
                            )
                        }
                    }
                }
                delay(16L)
            }
        }

        // --- GameDraw ---
        Box(modifier = Modifier.fillMaxSize()) {
            // Дороги
            roadPositions.forEach { y ->
                Image(
                    bitmap = roadImage,
                    contentDescription = "Road",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(with(density) { roadHeightPx.toDp() })
                        .offset(y = with(density) { y.toDp() }),
                    contentScale = ContentScale.FillBounds
                )
            }

            // Spawn
            Image(
                bitmap = spawnImage,
                contentDescription = "Spawn",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(with(density) { spawnHeightPx.toDp() })
                    .align(Alignment.BottomCenter),
                contentScale = ContentScale.FillBounds
            )

            // Cars
            cars.forEach { car ->
                val carModifier = Modifier
                    .size(
                        width = with(density) { car.widthPx.toDp() },
                        height = with(density) { car.heightPx.toDp() }
                    )
                    .offset(
                        x = with(density) { car.x.toDp() },
                        y = with(density) { (car.y - car.heightPx / 2f).toDp() }
                    )
                    .graphicsLayer(
                        scaleX = if (car.speed > 0) -1f else 1f,
                        scaleY = 1f
                    )

                Image(
                    bitmap = car.texture,
                    contentDescription = "Car",
                    modifier = carModifier
                )
            }

            // Coins
            coins.filterNot { it.isCollected }.forEach { coin ->
                Image(
                    bitmap = coinImage,
                    contentDescription = "Coin",
                    modifier = Modifier
                        .size((60 * baseScale).dp)
                        .offset(
                            x = with(density) { coin.x.toDp() },
                            y = with(density) { (coin.y - coinSizePx / 2).toDp() }
                        )
                )
            }

            // Player
            Image(
                bitmap = chickenImageState,
                contentDescription = "Chicken",
                modifier = Modifier
                    .size((60 * baseScale).dp)
                    .offset(
                        x = with(density) { chicken.x.toDp() },
                        y = with(density) { chicken.y.toDp() }
                    )
            )

            // Spawn decorations
            val spawnDecAspect = spawnDecoration.width.toFloat() / spawnDecoration.height.toFloat()
            val spawnDecHeightPx = screenWidthPx / spawnDecAspect
            Image(
                bitmap = spawnDecoration,
                contentDescription = "Spawn Decoration",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(with(density) { spawnDecHeightPx.toDp() })
                    .align(Alignment.BottomCenter),
                contentScale = ContentScale.FillBounds
            )

            // TopBar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = (16 * baseScale).dp, vertical = (24 * baseScale).dp)
                    .align(Alignment.TopCenter),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    RoundBtnWithIcon(
                        onClick = { if (!isGameOver) isPaused = !isPaused },
                        modifier = Modifier.size((60 * baseScale).dp),
                        painter = painterResource(R.drawable.ic_settings)
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    ScorePanel(
                        score = score,
                        showPlus = false
                    )
                }
            }

            // gamepad
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding((16 * baseScale).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ControlButton(rotation = 90f) { pressed ->
                    if (!isPaused && !isGameOver) chickenDir = if (pressed) Pair(0f, -1f) else Pair(
                        0f,
                        0f
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    ControlButton(rotation = 0f) { pressed ->
                        if (!isPaused && !isGameOver) chickenDir = if (pressed) Pair(
                            -1f,
                            0f
                        ) else Pair(0f, 0f)
                    }

                    Spacer(modifier = Modifier.width((60 * baseScale).dp))

                    ControlButton(rotation = 180f) { pressed ->
                        if (!isPaused && !isGameOver) chickenDir = if (pressed) Pair(
                            1f,
                            0f
                        ) else Pair(0f, 0f)
                    }
                }

                ControlButton(rotation = -90f) { pressed ->
                    if (!isPaused && !isGameOver) chickenDir = if (pressed) Pair(0f, 1f) else Pair(
                        0f,
                        0f
                    )
                }
            }

            // Пауза
            if (isPaused && !isGameOver) {
                PauseOverlay(
                    isPaused = isPaused,
                    onContinue = { isPaused = false },
                    onToggleMusic = {
                        localMusicEnabled = !localMusicEnabled; onToggleMusic()
                    },
                    onToggleSound = {
                        localSoundEnabled = !localSoundEnabled; onToggleSound()
                    },
                    onToggleVibration = {
                        localVibrationEnabled = !localVibrationEnabled; onToggleVibration()
                    },
                    onRestart = { onRestartRequest() },
                    onGoHome = { onMenuRequest() },
                    onClose = { isPaused = false },
                    musicEnabled = localMusicEnabled,
                    soundEnabled = localSoundEnabled,
                    vibrationEnabled = localVibrationEnabled
                )
            }

            // ===========================
            // 🔳 DEBUG COLLIDERS OVERLAY
            // ===========================
            if (showColliders) {
                Canvas(modifier = Modifier.fillMaxSize()) {

                    //Курка
                    run {
                        val b = shrunkBounds(
                            x = chicken.x,
                            y = chicken.y,
                            w = chickenSizePx,
                            h = chickenSizePx,
                            shrinkX = playerColliderShrinkX,
                            shrinkY = playerColliderShrinkY
                        )
                        drawRect(
                            color = Color(0x8022C55E),
                            topLeft = Offset(b.left, b.top),
                            size = Size(b.width, b.height),
                            style = Stroke(width = 3f)
                        )
                    }

                    //Машини
                    cars.forEach { car ->
                        val carTop = car.y - car.heightPx / 2f
                        val b = shrunkBounds(
                            x = car.x,
                            y = carTop,
                            w = car.widthPx,
                            h = car.heightPx,
                            shrinkX = carColliderShrink,
                            shrinkY = carColliderShrink
                        )
                        drawRect(
                            color = Color(0x80EF4444),
                            topLeft = Offset(b.left, b.top),
                            size = Size(b.width, b.height),
                            style = Stroke(width = 3f)
                        )
                    }

                    //Монети
                    coins.filterNot { it.isCollected }.forEach { coin ->
                        val b = Bounds(
                            left = coin.x,
                            top = coin.y - coinSizePx / 2f,
                            right = coin.x + coinSizePx,
                            bottom = coin.y + coinSizePx / 2f
                        )
                        drawRect(
                            color = Color(0x80F59E0B),
                            topLeft = Offset(b.left, b.top),
                            size = Size(b.width, b.height),
                            style = Stroke(width = 3f)
                        )
                    }
                }
            }
        }
    }
}

// ------------------------------------------------------------
// Фізичні колізії
// ------------------------------------------------------------
fun checkCollisionSimple(
    chicken: Chicken,
    chickenSizePx: Float,
    car: Car,
    playerShrinkX: Float,
    playerShrinkY: Float,
    carShrink: Float
): Boolean {
    val chickenB = shrunkBounds(
        x = chicken.x,
        y = chicken.y,
        w = chickenSizePx,
        h = chickenSizePx,
        shrinkX = playerShrinkX,
        shrinkY = playerShrinkY
    )

    val carTop = car.y - car.heightPx / 2f
    val carB = shrunkBounds(
        x = car.x,
        y = carTop,
        w = car.widthPx,
        h = car.heightPx,
        shrinkX = carShrink,
        shrinkY = carShrink
    )

    return intersects(chickenB, carB)
}

fun checkCoinPickup(
    chicken: Chicken,
    chickenSizePx: Float,
    coin: Coin,
    coinSizePx: Float
): Boolean {
    val chickenB = Bounds(
        left = chicken.x,
        top = chicken.y,
        right = chicken.x + chickenSizePx,
        bottom = chicken.y + chickenSizePx
    )
    val coinB = Bounds(
        left = coin.x,
        top = coin.y - coinSizePx / 2f,
        right = coin.x + coinSizePx,
        bottom = coin.y + coinSizePx / 2f
    )
    return intersects(chickenB, coinB)
}