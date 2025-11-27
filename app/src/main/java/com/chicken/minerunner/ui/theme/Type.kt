package com.chicken.minerunner.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.chicken.minerunner.R

val MineFont = FontFamily(Font(R.font.mochiy_pop_one_regular))

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = MineFont,
        fontWeight = FontWeight.Bold,
        fontSize = 38.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = MineFont,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = MineFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = MineFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    )
)
