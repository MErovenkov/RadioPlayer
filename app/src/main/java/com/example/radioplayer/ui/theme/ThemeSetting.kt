package com.example.radioplayer.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

data class MainColors(
    val primaryText: Color,
    val primaryBackground: Color,
    val progressBarColor: Color,
    val errorColor: Color,
    val rippleEffectColor: Color,
    val screenItemBackground: Color
)

data class MainTypography(
    val basic: TextStyle,
)

object MainTheme {
    val colors: MainColors
        @Composable
        get() = LocalColors.current

    val typography: MainTypography
        @Composable
        get() = LocalTypography.current
}

val LocalColors = staticCompositionLocalOf<MainColors> {
    error("No colors provided")
}

val LocalTypography = staticCompositionLocalOf<MainTypography> {
    error("No font provided")
}