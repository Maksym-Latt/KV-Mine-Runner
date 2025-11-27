package com.chicken.minerunner.ui.components

import android.R.attr.scaleY
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameTitle(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        GradientOutlinedText(
            text = "CHICKEN",
            fontSize = 64.sp,
            outlineWidth = 10f,
            outlineColor = Color(0xFF551A32),
            gradient = Brush.verticalGradient(
                listOf(Color(0xFFFF88D0), Color(0xFFCA3CC7))
            ),
        )
        GradientOutlinedText(
            text = "Mine",
            fontSize = 64.sp,
            outlineWidth = 10f,
            outlineColor = Color(0xFF551A32),
            gradient = Brush.verticalGradient(
                listOf(Color(0xFFFF88D0), Color(0xFFCA3CC7))
            ),
        )
        GradientOutlinedText(
            text = "RUNNER",
            gradient = Brush.verticalGradient(
                listOf(Color(0xFFFFC107), Color(0xFFFFC107))
            ),
            outlineColor = Color(0xFF6A3C00),
            fontSize = 30.sp,
            outlineWidth = 10f,
            modifier = Modifier
                .offset(y = (-50).dp)
        )
    }
}
