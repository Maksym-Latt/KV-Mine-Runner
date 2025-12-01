package com.chicken.dropper.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun IconAccentButton(
    iconPainter: Painter,
    onPress: () -> Unit,
    modifier: Modifier = Modifier,

    brush: Brush = Brush.horizontalGradient(
        listOf(
            Color(0xff286298),
            Color(0xff519ee4),
            Color(0xff296398)
        )
    ),
    strokeColor: Color = Color(0xff000000),

    size: Dp = 60.dp,
    iconScale: Dp = 30.dp,
    radius: Dp = 14.dp
) {
    Surface(
        onClick = onPress,
        shape = RoundedCornerShape(radius),
        color = Color.Transparent,
        border = BorderStroke(2.dp, strokeColor),
        modifier = modifier
            .size(size)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(radius),
                clip = false
            )
    ) {
        Box(
            modifier = Modifier
                .background(brush)
                .padding(size * 0.17f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = iconPainter,
                contentDescription = null,
                modifier = Modifier.size(iconScale)
            )
        }
    }
}
