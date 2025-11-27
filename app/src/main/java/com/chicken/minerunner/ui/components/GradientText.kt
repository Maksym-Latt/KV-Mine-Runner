package com.chicken.minerunner.ui.components

import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chicken.minerunner.ui.theme.TextGradient

@Composable
fun GradientText(
    text: String,
    modifier: Modifier = Modifier,
    brush: Brush = TextGradient,
    style: TextStyle = MaterialTheme.typography.displayLarge,
    outlineColor: Color = Color.Black,
    outlineWidth: Float = 3f,
    textAlign: TextAlign? = null
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = style.copy(
            brush = brush,
            shadow = androidx.compose.ui.text.Shadow(
                color = outlineColor,
                blurRadius = outlineWidth,
                offset = Offset(2f, 2f)
            ),
            textAlign = textAlign
        )
    )
}
