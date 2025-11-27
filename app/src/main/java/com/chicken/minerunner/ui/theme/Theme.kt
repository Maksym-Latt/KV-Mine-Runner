package com.chicken.minerunner.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = DeepBlue,
    secondary = SkyBlue,
    tertiary = Amber,
    background = CardSurface,
    surface = CardSurface,
    onPrimary = TextLight,
    onSecondary = TextLight,
    onTertiary = TextLight,
    onBackground = TextLight,
    onSurface = TextLight,
)

@Composable
fun MineRunnerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = mineRunnerTypography(),
        content = content
    )
}
