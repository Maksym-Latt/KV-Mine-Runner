package com.chicken.minerunner.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameTitleColumn(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        GradientText(
            text = "CHICKEN",
            size = 58.sp,
            stroke = 10f,
            strokeColor = Color(0xff1b1b1b),
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xff936400),
                    Color(0xffd1941a),
                    Color(0xff946400)
                )
            ),
        )
        GradientText(
            text = "MINE",
            size = 56.sp,
            stroke = 10f,
            strokeColor = Color(0xff1b1b1b),
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xff245785),
                    Color(0xff4a93d7),
                    Color(0xff245786)
                )
            ),
            modifier = Modifier
                .offset(y = (-40).dp)
        )
        GradientText(
            text = "RUNNER",
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xff245785),
                    Color(0xff4a93d7),
                    Color(0xff245786)
                )
            ),
            strokeColor = Color(0xff1b1b1b),
            size = 46.sp,
            stroke = 10f,
            modifier = Modifier
                .offset(y = (-70).dp)
        )
    }
}
