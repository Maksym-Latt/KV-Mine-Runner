package com.chicken.minerunner.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EggCounterBox(
    amount: Int,
    iconRes: Int,
    modifier: Modifier = Modifier,

    gradientBrush: Brush = Brush.horizontalGradient(
        listOf(
            Color(0xff387d07),
            Color(0xff94e324),
            Color(0xff387d07)
        )
    ),
    strokeColor: Color = Color(0xff000000),

    blockHeight: Dp = 60.dp,
    contentPaddingH: Dp = 18.dp,
    contentPaddingV: Dp = 6.dp,
    radius: Dp = 30.dp,
    shadow: Dp = 8.dp,
    iconSize: Dp = 36.dp
) {
    Surface(
        shape = RoundedCornerShape(radius),
        color = Color.Transparent,
        border = BorderStroke(2.dp, strokeColor),
        modifier = modifier
            .height(blockHeight)
            .shadow(
                elevation = shadow,
                shape = RoundedCornerShape(radius),
                clip = false
            )
            .wrapContentWidth()
    ) {
        Box(
            modifier = Modifier
                .background(gradientBrush)
                .padding(horizontal = contentPaddingH, vertical = contentPaddingV)
        ) {
            Row(
                modifier = Modifier.height(blockHeight - contentPaddingV * 2),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GradientText(
                    text = amount.toString(),
                    size = 26.sp,
                    stroke = 4f,
                    expand = false,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize)
                )
            }
        }
    }
}
