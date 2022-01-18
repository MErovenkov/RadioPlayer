package com.example.radioplayer.ui.screen.favoritelist

import androidx.compose.ui.platform.LocalContext
import com.example.radioplayer.ui.screen.favoritelist.view.FavoriteMusicDisplay
import com.example.radioplayer.ui.screen.view.ErrorMessage
import com.example.radioplayer.ui.screen.view.Loading
import com.example.radioplayer.util.state.UiState
import com.example.radioplayer.viewmodel.FavoriteMusicViewModel
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import com.example.radioplayer.R
import com.example.radioplayer.data.repository.database.model.FavoriteMusic
import com.example.radioplayer.ui.screen.view.ActionDialog

@Composable
fun FavoriteMusicListScreen(radioTitle: String,
                            favoriteMusicViewModel: FavoriteMusicViewModel
) {
    val viewState = favoriteMusicViewModel.favoriteMusicState.collectAsState()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current.applicationContext

    var deleteFavoriteMusic: FavoriteMusic? by remember { mutableStateOf(null) }

    LaunchedEffect(radioTitle) {
        favoriteMusicViewModel.findRadio(radioTitle)
    }

    var showDialog by remember { mutableStateOf(false)}

    when (val state = viewState.value) {
        is UiState.Loading -> Loading()
        is UiState.Success -> {

            FavoriteMusicDisplay(
                favoriteMusicList = state.resources.favoriteMusicList,
                onDelete =  { favoriteMusic ->
                    showDialog = true
                    deleteFavoriteMusic = favoriteMusic
                },
                onCopy = { favoriteMusicTitle ->
                    clipboardManager.setText(AnnotatedString(favoriteMusicTitle))

                    Toast.makeText(context,
                        context.resources.getString(R.string.message_copy_to_clipboard),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )

            if (showDialog) {
                ActionDialog(
                    title = stringResource(id = R.string.action_delete),
                    text = "${stringResource(id = R.string.action_delete)} ${deleteFavoriteMusic?.title}?",
                    actionTitle = stringResource(id = R.string.action_delete),
                    onResult = { choice ->
                        if (choice) {
                            deleteFavoriteMusic
                                ?.let { favoriteMusicViewModel.deleteFavoriteMusic(it) }
                        }
                        showDialog = false
                        deleteFavoriteMusic = null
                    }
                )
            }
        }
        is UiState.Error -> ErrorMessage(state.message)
    }
}