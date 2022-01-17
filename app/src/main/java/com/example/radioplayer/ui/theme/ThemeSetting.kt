package com.example.radioplayer.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

data class MainColors(
    val primaryText: Color,
    val primaryBackground: Color,
    val progressBar: Color,
    val error: Color,
    val rippleEffect: Color,
    val screenItemBackground: Color,
    val divider: Color,
    val tintIcon: Color
)

data class MainTypography(
    val heading: TextStyle,
    val headingSmall: TextStyle,
    val basic: TextStyle,
    val button: TextStyle,
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