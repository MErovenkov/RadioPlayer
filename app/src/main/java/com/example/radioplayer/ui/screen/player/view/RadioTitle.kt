package com.example.radioplayer.ui.screen.player.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.radioplayer.ui.theme.MainTheme
import com.example.radioplayer.util.state.MusicState

@Composable
fun RadioTitle(radioTitle: String,
               musicState: MusicState,
               onChangeFavorite: (musicState: MusicState) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp),
           verticalArrangement = Arrangement.Center,
           horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = radioTitle,
            textAlign = TextAlign.Center,
            style = MainTheme.typography.heading,
            color = MainTheme.colors.primaryText)

        MusicTitle(musicState) { onChangeFavorite(musicState) }
    }
}

@Preview
@Composable
fun RadioTitlePreview() {
    MainTheme(darkTheme = false) {
        RadioTitle(radioTitle = "ROCK FM",
                   musicState = MusicState.Favorite(
                       "Hidenpelto including Hapean Hiljaiset Vedet - " +
                            "Field Of The Devil include The Silent Waters Of Infamy")
        ) { }
    }
}