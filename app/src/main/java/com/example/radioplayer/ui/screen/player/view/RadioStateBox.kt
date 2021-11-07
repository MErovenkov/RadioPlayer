package com.example.radioplayer.ui.screen.player.view

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.radioplayer.ui.screen.view.ErrorMessage
import com.example.radioplayer.util.state.PlayerState
import com.example.radioplayer.ui.screen.view.Loading
import com.example.radioplayer.ui.theme.MainTheme

@Composable
fun RadioStateBox(playerState: PlayerState) {
    var targetValue by remember { mutableStateOf(20.dp) }

    val animationProgress by animateDpAsState(
        targetValue = targetValue,
        infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    SideEffect {
        targetValue = 5.dp
    }

    Surface(modifier = Modifier,
        elevation = if (playerState.isPlaying) { animationProgress } else targetValue,
        shape = CircleShape,
        color = MainTheme.colors.screenItemBackground
    ) {
        Box(
            modifier = Modifier.size(300.dp),
            contentAlignment = Alignment.Center
        ) {
            when (playerState) {
                is PlayerState.Buffering -> Loading()
                is PlayerState.Error -> ErrorMessage(message = playerState.message)
                is PlayerState.Playing -> RadioTitle(radioTitle = playerState.radioTitle,
                                                     musicTitle = playerState.musicTitle)
                is PlayerState.Pause -> RadioTitle(radioTitle = playerState.radioTitle,
                                                   musicTitle = playerState.musicTitle)
            }
        }
    }
}

@Preview
@Composable
fun RadioStateBoxPreview() {
    MainTheme(darkTheme = false) {
        RadioStateBox(
            PlayerState.Playing(radioTitle = "JAZ FM",
                             musicTitle = "Hiidenpelto including Hapean Hiljaiset Vedet - " +
                                          "Field Of The Devil include The Silent Waters Of Infamy")
        )
    }
}