package com.example.radioplayer.ui.screen.favoritelist.view

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.radioplayer.data.repository.database.model.FavoriteMusic
import com.example.radioplayer.ui.theme.MainTheme

@Composable
fun FavoriteItem(favoriteMusic: FavoriteMusic,
                 onDelete: (favoriteMusic: FavoriteMusic) -> Unit,
                 onCopy: (favoriteMusicTitle: String) -> Unit) {

    var isRevealed by remember { mutableStateOf(false) }
    var cardOffset by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxWidth()) {

        ActionsRow(
            modifier = Modifier.onSizeChanged { cardOffset = it.width }
                               .align(Alignment.CenterEnd)
                               .padding(end = 8.dp),
            onDelete = { onDelete(favoriteMusic) },
            onCopy = { onCopy(favoriteMusic.title) }
        )

        DraggableCard(
            musicTitle = favoriteMusic.title,
            isRevealed = isRevealed,
            cardOffset = cardOffset,
            onRevealed = { isRevealed = it }
        )
    }
}

@Preview
@Composable
fun FavoriteItemPreview() {
    MainTheme(darkTheme = false) {
        FavoriteItem(
            favoriteMusic = FavoriteMusic(title = "Nita Strauss, David Draiman, Disturbed " +
                    "- Dead Inside", radioId = 0),
            onDelete = { },
            onCopy = { }
        )
    }
}
