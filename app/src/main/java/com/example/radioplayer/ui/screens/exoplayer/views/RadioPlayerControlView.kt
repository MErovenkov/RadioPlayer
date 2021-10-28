package com.example.radioplayer.ui.screens.exoplayer.views

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
import com.google.android.exoplayer2.Player

@Composable
fun RadioPlayerControlView(title: String, playerState: PlayerState, onPlayPause: () -> Unit) {
    Surface(color = MainTheme.colors.primaryBackground) {
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RadioStateBox(
                title = title,
                playerState = playerState
            )

            PlayPauseButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .absolutePadding(0.dp, 0.dp, 20.dp, 0.dp),
                isPlaying = playerState.isPlaying.value,
                onClick = onPlayPause
            )
        }
    }
}

@Preview
@Composable
fun RadioPlayerControlViewPreview() {
    MainTheme(darkTheme = false) {
        RadioPlayerControlView("RETRO FM",
            PlayerState().apply {
                isPlaying.value = true
                playbackState.value = Player.STATE_READY
            }
        ) { }
    }
}