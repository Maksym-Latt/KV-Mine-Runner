package com.chicken.minerunner.ui.screens

import android.R.attr.translationY
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.minerunner.R
import com.chicken.minerunner.ui.components.GameTitleColumn
import com.chicken.minerunner.ui.components.GradientText
import com.chicken.minerunner.ui.theme.CopperDark
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(onFinished: () -> Unit) {

    val appear = remember { Animatable(0f) }
    val dots = remember { Animatable(0f) }

    LaunchedEffect(Unit) {

        appear.animateTo(
            targetValue = 1f,
            animationSpec = tween(900, easing = FastOutSlowInEasing)
        )

        launch {
            dots.animateTo(
                targetValue = 3f,
                animationSpec = infiniteRepeatable(
                    animation = tween(900, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )
        }

        delay(2000)
        onFinished()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.bg_menu),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .graphicsLayer {
                    alpha = appear.value
                    translationY = (1f - appear.value) * 40f
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            GameTitleColumn()

            Spacer(modifier = Modifier.height(32.dp))

            Image(
                painter = painterResource(R.drawable.chicken_title),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .aspectRatio(0.85f),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(36.dp))

            val dotsCount = (dots.value % 3).toInt() + 1
            val dotsText = ".".repeat(dotsCount)

            GradientText(
                text = "Loading$dotsText",
                strokeColor = Color.Black,
                brush = Brush.verticalGradient(
                    listOf(Color.White, Color(0xFFFFE082))
                ),
                size = 24.sp
            )
        }
    }
}
