package com.example.radioplayer.ui.screen.player.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.radioplayer.util.state.PlayerState
import com.example.radioplayer.ui.theme.MainTheme
import com.example.radioplayer.util.state.MusicState

@Composable
fun RadioPlayerControlView(playerState: PlayerState,
                           onPlayPause: (isPlay: Boolean) -> Unit,
                           onChangeFavorite: (musicState: MusicState) -> Unit
) {
    Surface(color = MainTheme.colors.primaryBackground) {
        Column(modifier = Modifier.fillMaxSize(),
               verticalArrangement = Arrangement.Center,
               horizontalAlignment = Alignment.CenterHorizontally
        ) {

            RadioStateBox(
                playerState = playerState,
                onChangeFavorite = { musicState -> onChangeFavorite(musicState) }
            )

            PlayPauseButton(modifier = Modifier.align(Alignment.End)
                                               .absolutePadding(0.dp, 0.dp, 20.dp, 0.dp),
                            isPlaying = playerState.isPlaying,
                            onClick = { onPlayPause(it) }
            )
        }
    }
}

@Preview
@Composable
fun RadioPlayerControlViewPreview() {
    MainTheme(darkTheme = false) {
        RadioPlayerControlView(
                playerState = PlayerState.Playing("JAZ FM", MusicState.Other()),
                onPlayPause = { },
                onChangeFavorite = { }
        )
    }
}