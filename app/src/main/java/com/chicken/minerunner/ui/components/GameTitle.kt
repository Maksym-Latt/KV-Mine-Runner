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
            fontSize = 58.sp,
            outlineWidth = 10f,
            outlineColor = Color(0xff1b1b1b),
            gradient = Brush.verticalGradient(
                colors = listOf(
                    Color(0xff936400),
                    Color(0xffd1941a),
                    Color(0xff946400)
                )
            ),
        )
        GradientOutlinedText(
            text = "MINE",
            fontSize = 56.sp,
            outlineWidth = 10f,
            outlineColor = Color(0xff1b1b1b),
            gradient = Brush.verticalGradient(
                colors = listOf(
                    Color(0xff245785),
                    Color(0xff4a93d7),
                    Color(0xff245786)
                )
            ),
            modifier = Modifier
                .offset(y = (-40).dp)
        )
        GradientOutlinedText(
            text = "RUNNER",
            gradient = Brush.verticalGradient(
                colors = listOf(
                    Color(0xff245785),
                    Color(0xff4a93d7),
                    Color(0xff245786)
                )
            ),
            outlineColor = Color(0xff1b1b1b),
            fontSize = 46.sp,
            outlineWidth = 10f,
            modifier = Modifier
                .offset(y = (-70).dp)
        )
    }
}
