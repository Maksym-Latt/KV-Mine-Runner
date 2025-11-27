package com.chicken.dropper.ui.splashscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.unit.dp
import com.chicken.dropper.R
import com.chicken.dropper.ui.components.GameTitle

@Composable
internal fun SplashScreen(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_menu),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            GameTitle(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer { alpha = 0.98f }
            )
            Image(
                painter = painterResource(id = R.drawable.chicken_1_egg),
                contentDescription = "Chicken mascot",
                modifier = Modifier
                    .fillMaxWidth(0.64f),
                contentScale = ContentScale.Fit
            )
            AnimatedLoadingText()
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
