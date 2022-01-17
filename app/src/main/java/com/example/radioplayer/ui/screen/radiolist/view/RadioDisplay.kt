package com.example.radioplayer.ui.screen.radiolist.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.radioplayer.ui.screen.view.GenericLazyColumn
import com.example.radioplayer.ui.theme.MainTheme
import com.example.radioplayer.util.state.NavigationState
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata

@Composable
fun RadioDisplay(radioItemList: List<MediaItem>,
                 onClick: (navigationState: NavigationState<String>) -> Unit) {

    GenericLazyColumn(
        modifier = Modifier.fillMaxSize(),
        list = radioItemList) { radioItem ->

        RadioCardItem(
            radioItem = radioItem,
            onClick = { navigationState -> onClick(navigationState) }
        )
    }
}

@Preview
@Composable
fun RadioListDisplayPreview() {
    MainTheme(darkTheme = false) {
        RadioDisplay(
            radioItemList = listOf(
                MediaItem.Builder()
                    .setUri("uri")
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle("RETRO FM")
                            .build()
                    )
                    .build(),
                MediaItem.Builder()
                    .setUri("uri")
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle("JAZ FM")
                            .build()
                    )
                    .build()
            )
        ) { }
    }
}