package com.chicken.minerunner.ui.components

import android.R.attr.text
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EggCounter(
    count: Int,
    eggIcon: Int,
    modifier: Modifier = Modifier,

    gradient: Brush = Brush.horizontalGradient(
        listOf(
            Color(0xff387d07),
            Color(0xff94e324),
            Color(0xff387d07)
        )
    ),
    borderColor: Color = Color(0xff000000),

    height: Dp = 60.dp,
    horizontalPadding: Dp = 18.dp,
    verticalPadding: Dp = 6.dp,
    cornerRadius: Dp = 30.dp,
    elevation: Dp = 8.dp,
    eggIconSize: Dp = 36.dp
) {
    Surface(
        shape = RoundedCornerShape(cornerRadius),
        color = Color.Transparent,
        border = BorderStroke(2.dp, borderColor),
        modifier = modifier
            .height(height)
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(cornerRadius),
                clip = false
            )
            .wrapContentWidth()
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .padding(horizontal = horizontalPadding, vertical = verticalPadding)
        ) {
            Row(
                modifier = Modifier.height(height - verticalPadding * 2),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GradientOutlinedText(
                    text = count.toString(),
                    fontSize = 26.sp,
                    outlineWidth = 4f,
                    fillWidth = false,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(id = eggIcon),
                    contentDescription = null,
                    modifier = Modifier.size(eggIconSize)
                )
            }
        }
    }
}
