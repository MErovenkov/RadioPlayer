package com.example.radioplayer.ui.screen.favoritelist.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.radioplayer.data.repository.database.model.FavoriteMusic
import com.example.radioplayer.ui.screen.view.GenericLazyColumn
import com.example.radioplayer.ui.theme.MainTheme

@Composable
fun FavoriteMusicDisplay(favoriteMusicList: List<FavoriteMusic>,
                         onDelete: (favoriteMusic: FavoriteMusic) -> Unit,
                         onCopy: (favoriteMusicTitle: String) -> Unit) {
    GenericLazyColumn(modifier = Modifier.fillMaxSize(),
                      list = favoriteMusicList) { favoriteMusic ->
        FavoriteItem(
            favoriteMusic = favoriteMusic,
            onDelete = { onDelete(it) },
            onCopy = { favoriteMusicTitle -> onCopy(favoriteMusicTitle) }
        )
    }
}

@Preview
@Composable
fun FavoriteMusicDisplayPreview() {
    MainTheme(darkTheme = false) {
        FavoriteMusicDisplay(
            favoriteMusicList = arrayListOf(
                FavoriteMusic(title = " Nita Strauss, David Draiman, Disturbed - Dead Inside - ",
                    radioId = 0),
                FavoriteMusic(title = "Five Finger Death Punch - The Devil's Own",
                    radioId = 0)),
            onDelete = { },
            onCopy = { }
        )
    }
}