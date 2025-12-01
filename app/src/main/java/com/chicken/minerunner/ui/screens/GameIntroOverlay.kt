package com.chicken.minerunner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.dropper.ui.components.ActionButton
import com.chicken.dropper.ui.components.ChickenButtonStyleVariant
import com.chicken.minerunner.ui.components.GradientText

@Composable
fun GameIntroOverlay(onStart: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            GradientText(
                text = "How to Play",
                size = 36.sp,
                stroke = 7f,
                strokeColor = Color.Black,
                brush = Brush.horizontalGradient(
                    listOf(Color(0xFFFFD36E), Color(0xFFFFA726))
                )
            )

            Text(
                text = "Swipe left or right to change rails.\n" +
                        "Swipe up to run forward and collect eggs.\n" +
                        "Avoid trolleys — or grab a helmet to survive the hit!",
                color = Color.White,
                fontSize = 18.sp,
                lineHeight = 22.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            ActionButton(
                label = "START RUN",
                onPress = onStart,
                labelSize = 22.sp,
                variant = ChickenButtonStyleVariant.Blue,
                modifier = Modifier.fillMaxWidth(0.75f)
            )
        }
    }
}
