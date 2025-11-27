package com.chicken.dropper.ui.splashscreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.chicken.dropper.ui.components.GradientOutlinedText
import kotlinx.coroutines.delay

@Composable
fun AnimatedLoadingText(
    modifier: Modifier = Modifier,
    base: String = "LOADING",
    stepMs: Long = 300L
) {
    val order = intArrayOf(1, 2, 3, 0)
    var idx by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(stepMs)
            idx = (idx + 1) % order.size
        }
    }
    val dots = order[idx]

    val display = buildString {
        append(base)
        if (dots > 0) append(" ")
        repeat(dots) { append(".") }
    }

    GradientOutlinedText(
        text = display,
        gradient = Brush.verticalGradient(
            listOf(Color(0xFFFFC107), Color(0xFFFFA200))
        ),
        outlineColor = Color(0xFF6A3C00),
        fontSize = 38.sp,
        outlineWidth = 10f,
    )
}