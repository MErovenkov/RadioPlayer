package com.example.radioplayer.ui.screen.list.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.radioplayer.ui.navigation.Navigation
import com.example.radioplayer.ui.theme.MainTheme
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata

@Composable
fun RadioListDisplay(navController: NavController, radioItemList: List<MediaItem>) {
    Surface(color = MainTheme.colors.primaryBackground) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            radioItemList.forEach { radioItem ->
                item {
                    RadioCardItem(radioItem) {
                        navController.navigate(
                            "${Navigation.RADIO_PLAYER_SCREEN}/${radioItem.mediaMetadata.title}"
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun RadioListDisplayPreview() {
    MainTheme(darkTheme = false) {
        RadioListDisplay(
            navController = rememberNavController(),
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
        )
    }
}