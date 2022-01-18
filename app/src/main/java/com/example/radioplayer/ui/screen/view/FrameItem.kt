package com.example.radioplayer.ui.screen.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.radioplayer.ui.theme.MainTheme

@Composable
fun FrameItem(modifier: Modifier,
              padding: Dp = 16.dp,
              content: @Composable () -> Unit
) {
    Surface(modifier = modifier.padding(horizontal = padding, vertical = padding / 2),
            elevation = 8.dp,
            shape = RoundedCornerShape(8.dp),
            color = MainTheme.colors.screenItemBackground,
            content = content
    )
}

@Composable
@Preview
fun FrameItemPreview() {
    MainTheme(darkTheme = false) {
        FrameItem(modifier = Modifier.width(150.dp).height(50.dp)) { }
    }
}