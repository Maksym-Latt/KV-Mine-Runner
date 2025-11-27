package com.chicken.dropper.ui.components

import android.R.attr.text
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.minerunner.ui.components.GradientOutlinedText

enum class ChickenButtonStyle {
    Green, Blue
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: ChickenButtonStyle = ChickenButtonStyle.Green,
    fontSize: TextUnit = 32.sp,
    content: (@Composable RowScope.() -> Unit)? = null
) {
    val (border, gradientMain) = when (style) {
        ChickenButtonStyle.Green ->
            Pair(
                Color(0xFF1F3406),
                listOf(Color(0xFF9FEF26), Color(0xFF448B0D))
            )

        ChickenButtonStyle.Blue ->
            Pair(
                Color(0xff091344),
                listOf(Color(0xff5558eb), Color(0xFF7E98A4))
            )
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clickable(onClick = onClick)
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
                    Brush.verticalGradient(gradientMain)
                )
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            if (content != null) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            } else {
                GradientOutlinedText(
                    text = text.uppercase(),
                    fontSize = fontSize,
                    outlineWidth = 4f,
                    outlineColor = Color(0xff000000),
                    gradient = Brush.verticalGradient(
                        listOf(Color(0xFFEDE622), Color(0xFFE1AC14))
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

