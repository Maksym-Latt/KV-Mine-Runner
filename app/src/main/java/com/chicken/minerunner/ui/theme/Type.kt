package com.chicken.minerunner.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.chicken.minerunner.R

private val MineFontFamily = FontFamily(Font(R.font.mochiy_pop_one_regular))

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = MineFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        lineHeight = 44.sp
    ),
    titleLarge = TextStyle(
        fontFamily = MineFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 32.sp
    ),
    titleMedium = TextStyle(
        fontFamily = MineFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 26.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = MineFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    labelLarge = TextStyle(
        fontFamily = MineFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 18.sp
    )
)
