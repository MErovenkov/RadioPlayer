package com.example.radioplayer.ui.screen.player.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.radioplayer.ui.theme.MainTheme
import com.example.radioplayer.util.state.MusicState
import com.example.radioplayer.util.state.PlayerState

@Composable
fun RadioPlayerDisplay(playerState: PlayerState,
                       onPlayPause: (isPlay: Boolean) -> Unit,
                       onChangeFavorite: (musicState: MusicState) -> Unit) {

    RadioPlayerControlView(
        playerState = playerState,
        onPlayPause = { isPlaying -> onPlayPause(isPlaying) },
        onChangeFavorite = { musicState -> onChangeFavorite(musicState) }
    )
}

@Preview
@Composable
fun RadioPlayerDisplayPreview() {
    MainTheme(darkTheme = false) {
        RadioPlayerControlView(
            playerState = PlayerState.Playing("JAZ FM", MusicState.Other("News")),
            onPlayPause = { },
            onChangeFavorite = { }
        )
    }
}