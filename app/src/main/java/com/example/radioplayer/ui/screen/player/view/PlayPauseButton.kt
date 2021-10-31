package com.example.radioplayer.ui.screen.player.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.radioplayer.R
import com.example.radioplayer.ui.theme.MainTheme

@Composable
fun PlayPauseButton(modifier: Modifier, isPlaying: Boolean, onClick: () -> Unit) {
    Button(
        modifier = modifier
            .width(80.dp)
            .height(80.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MainTheme.colors.screenItemBackground),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp,
            disabledElevation = 0.dp),
        onClick = onClick
    ) {
        Image(
            painter = painterResource(
                id = when (isPlaying) {
                    true -> R.drawable.ic_exo_pause
                    false -> R.drawable.ic_exo_play
                }
            ),
            contentDescription = stringResource(id = R.string.button_play_pause)
        )
    }
}

@Preview
@Composable
fun PlayPauseButtonPreview() {
    MainTheme(darkTheme = false) {
        PlayPauseButton(Modifier, true) { }
    }
}