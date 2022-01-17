package com.example.radioplayer.ui.screen.view

import androidx.compose.foundation.layout.Box
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.radioplayer.ui.theme.MainTheme

@Composable
fun Loading(modifier: Modifier = Modifier,
            strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth
) {
    Box(modifier = modifier) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center),
                                  color = MainTheme.colors.progressBar,
                                  strokeWidth = strokeWidth
        )
    }
}

@Preview
@Composable
fun RadioListLoadingPreview() {
    MainTheme(darkTheme = false) {
        Loading()
    }
}