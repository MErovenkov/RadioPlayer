package com.example.radioplayer.ui.screen.radiolist.view

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.radioplayer.ui.navigation.Navigation
import com.example.radioplayer.ui.screen.view.FrameItem
import com.example.radioplayer.ui.theme.MainTheme
import com.example.radioplayer.util.state.NavigationState
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata

@Composable
fun RadioCardItem(radioItem: MediaItem,
                  onClick: (navigationState: NavigationState<String>) -> Unit) {
    FrameItem(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            RadioTitleButton(
                modifier = Modifier.fillMaxSize().weight(4f),
                radioTitle = radioItem.mediaMetadata.title.toString(),
                onClick = { onClick(NavigationState(Navigation.RADIO_PLAYER_SCREEN,
                                                    radioItem.mediaMetadata.title.toString()))
                }
            )
        }
    }
}

@Preview
@Composable
fun RadioCardItemPreview() {
    MainTheme(darkTheme = false) {
        RadioCardItem(
            MediaItem.Builder()
                .setUri("uri")
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle("RETRO FM")
                        .build()
                )
                .build()
        ) { }
    }
}