package com.chicken.minerunner.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.chicken.minerunner.R

val MochiyPop = FontFamily(
    Font(R.font.mochiy_pop_one_regular, weight = FontWeight.Normal)
)

@Composable
fun mineRunnerTypography() = Typography.copy(
    displayLarge = Typography.displayLarge.copy(fontFamily = MochiyPop),
    displayMedium = Typography.displayMedium.copy(fontFamily = MochiyPop),
    displaySmall = Typography.displaySmall.copy(fontFamily = MochiyPop),
    headlineLarge = Typography.headlineLarge.copy(fontFamily = MochiyPop),
    headlineMedium = Typography.headlineMedium.copy(fontFamily = MochiyPop),
    headlineSmall = Typography.headlineSmall.copy(fontFamily = MochiyPop),
    titleLarge = Typography.titleLarge.copy(fontFamily = MochiyPop),
    titleMedium = Typography.titleMedium.copy(fontFamily = MochiyPop),
    titleSmall = Typography.titleSmall.copy(fontFamily = MochiyPop),
    bodyLarge = Typography.bodyLarge.copy(fontFamily = MochiyPop),
    bodyMedium = Typography.bodyMedium.copy(fontFamily = MochiyPop),
    bodySmall = Typography.bodySmall.copy(fontFamily = MochiyPop),
    labelLarge = Typography.labelLarge.copy(fontFamily = MochiyPop),
    labelMedium = Typography.labelMedium.copy(fontFamily = MochiyPop),
    labelSmall = Typography.labelSmall.copy(fontFamily = MochiyPop)
)
