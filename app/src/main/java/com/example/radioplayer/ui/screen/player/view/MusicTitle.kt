package com.example.radioplayer.ui.screen.player.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.radioplayer.R
import com.example.radioplayer.ui.screen.view.Loading
import com.example.radioplayer.ui.theme.MainTheme
import com.example.radioplayer.util.state.MusicState

@Composable
fun MusicTitle(musicState: MusicState,
               onChangeFavorite: (musicState: MusicState) -> Unit) {

    Row(modifier = Modifier.wrapContentHeight()
                           .clickable {
                               if (musicState.isMusic()) {
                                   onChangeFavorite(musicState)
                               }
                           },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        when (musicState) {
            is MusicState.Favorite -> {
                Image(painter = painterResource(R.drawable.ic_favorite),
                      contentDescription = stringResource(id = R.string.button_favorite))
            }

            is MusicState.Usual -> {
                Image(painter = painterResource(R.drawable.ic_usual),
                      contentDescription = stringResource(id = R.string.button_usual))
            }

            is MusicState.Unknown -> Loading(modifier = Modifier.size(24.dp).padding(end = 2.dp),
                                             strokeWidth = 2.dp)

            is MusicState.Other -> { }
        }

        Text(
            text = musicState.musicTitle,
            textAlign = TextAlign.Center,
            style = MainTheme.typography.basic,
            color = MainTheme.colors.primaryText
        )
    }
}

@Preview
@Composable
fun MusicTitlePreview() {
    MainTheme(darkTheme = false) {
        MusicTitle(
            musicState = MusicState.Favorite("Five Finger Death Punch - The Devil's Own"),
            onChangeFavorite = {  }
        )
    }
}