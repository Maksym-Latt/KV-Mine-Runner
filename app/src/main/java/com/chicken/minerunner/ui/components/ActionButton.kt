package com.chicken.dropper.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.minerunner.ui.components.GradientText

enum class ChickenButtonStyleVariant {
    Green, Blue
}

@Composable
fun ActionButton(
    label: String,
    onPress: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ChickenButtonStyleVariant = ChickenButtonStyleVariant.Green,
    labelSize: TextUnit = 32.sp,
    extraContent: (@Composable RowScope.() -> Unit)? = null
) {
    val (border, gradientMain) = when (variant) {
        ChickenButtonStyleVariant.Green ->
            Pair(
                Color(0xff050404),
                listOf(
                    Color(0xff387d07),
                    Color(0xffa6f628),
                    Color(0xff397e08)
                )
            )

        ChickenButtonStyleVariant.Blue ->
            Pair(
                Color(0xff020000),
                listOf(
                    Color(0xff286298),
                    Color(0xff519ee4),
                    Color(0xff296398)
                )
            )
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clickable(onClick = onPress)
            .shadow(
                elevation = 14.dp,
                shape = RoundedCornerShape(22.dp),
                ambientColor = Color.Black.copy(alpha = 0.4f),
                spotColor = Color.Black.copy(alpha = 0.4f)
            ),
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(2.dp, border),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(gradientMain)
                )
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            if (extraContent != null) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    content = extraContent
                )
            } else {
                GradientText(
                    text = label.uppercase(),
                    size = labelSize,
                    stroke = 4f,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

