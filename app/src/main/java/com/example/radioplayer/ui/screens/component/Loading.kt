package com.example.radioplayer.ui.screens.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.radioplayer.ui.theme.MainTheme

@Composable
fun Loading() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = MainTheme.colors.progressBarColor
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