package com.example.radioplayer.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun MainTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = when (darkTheme) {
        true -> baseDarkPalette
        false -> baseLightPalette
    }

    val typography = MainTypography(
        heading = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Normal
        ),

        headingSmall = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        ),

        basic = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        ),

        button = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    )

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTypography provides typography,
        content = content
    )
}