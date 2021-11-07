package com.example.radioplayer.ui.screen.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.radioplayer.ui.theme.MainTheme

@Composable
fun RadioTitle(titleRadio: String, titleMusic: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier,
            text = titleRadio,
            textAlign = TextAlign.Center,
            style = MainTheme.typography.heading,
            color = MainTheme.colors.primaryText
        )

        Text(
            modifier = Modifier,
            text = titleMusic,
            textAlign = TextAlign.Center,
            style = MainTheme.typography.basic,
            color = MainTheme.colors.primaryText
        )
    }
}

@Preview
@Composable
fun RadioTitlePreview() {
    MainTheme(darkTheme = false) {
        RadioTitle(titleRadio = "ROCK FM",
                   titleMusic = "Type O Negative - " +
                           "Unsuccessfully Coping With The Natural Beauty Of Infidelity")
    }
}