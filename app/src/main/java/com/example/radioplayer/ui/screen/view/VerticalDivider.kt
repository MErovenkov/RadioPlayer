package com.example.radioplayer.ui.screen.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.radioplayer.ui.theme.MainTheme

private const val DividerAlpha = 0.12f

@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onSurface.copy(alpha = DividerAlpha),
    thickness: Dp = 1.dp,
) {
    Box(modifier.fillMaxHeight()
                .width(thickness)
                .background(color = color)
    )
}

@Preview
@Composable
fun VerticalDividerPreview() {
    MainTheme(darkTheme = false) {
        VerticalDivider()
    }
}