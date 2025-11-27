package com.chicken.minerunner.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.chicken.minerunner.R

@Composable
fun GradientOutlinedText(
    text: String,
    modifier: Modifier = Modifier,

    fillWidth: Boolean = true,
    textAlign: TextAlign = TextAlign.Center,

    fontSize: TextUnit = 48.sp,
    outlineWidth: Float = 8f,
    outlineColor: Color = Color(0xff332000),

    gradient: Brush = Brush.horizontalGradient(
        colors = listOf(
            Color(0xffffffff),
            Color(0xffffffff),
            Color(0xfffffffe)
        )
    )
) {
    val fontFamily = remember {
        FontFamily(Font(R.font.mochiy_pop_one_regular, weight = FontWeight.ExtraBold))
    }

    val styledText = MaterialTheme.typography.displayLarge.copy(
        fontSize = fontSize,
        fontWeight = FontWeight.ExtraBold,
        fontFamily = fontFamily,
        textAlign = textAlign
    )

    val internalModifier = if (fillWidth) Modifier.fillMaxWidth() else Modifier

    Box(modifier = modifier) {

        val gradientText = buildAnnotatedString {
            withStyle(SpanStyle(brush = gradient)) { append(text) }
        }

        Text(
            text = text,
            style = styledText.copy(
                color = outlineColor,
                drawStyle = Stroke(
                    width = outlineWidth,
                    join = StrokeJoin.Round
                )
            ),
            modifier = internalModifier
        )

        Text(
            text = gradientText,
            style = styledText,
            color = Color.White,
            modifier = internalModifier
        )
    }
}
