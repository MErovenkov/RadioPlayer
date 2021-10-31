package com.example.radioplayer.ui.screen.player.view

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.radioplayer.R
import com.example.radioplayer.util.state.PlayerState
import com.example.radioplayer.ui.screen.view.ErrorMessage
import com.example.radioplayer.ui.screen.view.Loading
import com.example.radioplayer.ui.theme.MainTheme
import com.example.radioplayer.util.CheckStatusNetwork
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player

@Composable
fun RadioStateBox(title: String, playerState: PlayerState) {
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
        elevation = if (playerState.isPlaying.value) { animationProgress } else targetValue,
        shape = CircleShape,
        color = MainTheme.colors.screenItemBackground
    ) {
        Box(
            modifier = Modifier.size(300.dp),
            contentAlignment = Alignment.Center
        ) {
            when (playerState.playbackState.value) {
                Player.STATE_IDLE -> {
                    ErrorMessage(message = when (playerState.errorCode.value) {
                        PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> {
                            if (!CheckStatusNetwork.isActive) R.string.error_no_internet_access
                            else R.string.error_radio_station_unavailable
                        }

                        PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT ->
                            R.string.error_radio_station_connection_timeout

                        PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND ->
                            R.string.error_file_not_found

                        else -> R.string.error_unknown
                    })
                }
                Player.STATE_BUFFERING -> Loading()
                Player.STATE_READY -> {
                    Text(modifier = Modifier,
                        text = title,
                        fontSize = 30.sp,
                        style = MainTheme.typography.basic,
                        color = MainTheme.colors.primaryText
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun RadioStateBoxPreview() {
    MainTheme(darkTheme = false) {
        RadioStateBox("JAZ FM",
            PlayerState().apply {
                isPlaying.value = true
                playbackState.value = Player.STATE_READY
            }
        )
    }
}