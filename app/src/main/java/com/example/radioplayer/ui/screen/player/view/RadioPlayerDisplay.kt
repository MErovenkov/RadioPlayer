package com.example.radioplayer.ui.screen.player.view

import androidx.compose.runtime.Composable
import com.example.radioplayer.util.state.PlayerState

@Composable
fun RadioPlayerDisplay(playerState: PlayerState, onPlayPause: (isPlay: Boolean) -> Unit) {
    RadioPlayerControlView(playerState = playerState) { onPlayPause(it) }
}