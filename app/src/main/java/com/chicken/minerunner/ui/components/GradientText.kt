package com.chicken.minerunner.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit

@Composable
fun GradientText(
    text: String,
    modifier: Modifier = Modifier,
    gradient: Brush,
    outlineColor: Color,
    style: TextStyle = MaterialTheme.typography.titleLarge,
    fontSize: TextUnit = style.fontSize,
    textAlign: TextAlign = TextAlign.Center
) {
    Box(modifier = modifier) {
        Text(
            text = text,
            style = style.copy(
                color = outlineColor,
                fontSize = fontSize,
                drawStyle = Stroke(width = 6f),
                textAlign = textAlign
            ),
            textAlign = textAlign
        )
        Text(
            text = text,
            style = style.copy(
                brush = gradient,
                fontSize = fontSize,
                textAlign = textAlign
            ),
            textAlign = textAlign
        )
    }
}
