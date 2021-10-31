package com.example.radioplayer.ui.screen.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.example.radioplayer.util.state.UiState
import com.example.radioplayer.ui.screen.player.view.RadioPlayerDisplay
import com.example.radioplayer.ui.screen.view.ErrorMessage
import com.example.radioplayer.ui.screen.view.Loading
import com.example.radioplayer.viewmodel.DetailRadioViewModel

@Composable
fun RadioPlayerScreen(title: String, detailRadioViewModel: DetailRadioViewModel) {
    val viewState = detailRadioViewModel.radioState.collectAsState()

    LaunchedEffect(title) {
        detailRadioViewModel.findRadioItem(title)
    }

    when (val state = viewState.value) {
        is UiState.Loading -> Loading()
        is UiState.Success -> RadioPlayerDisplay(state.resources)
        is UiState.Error -> ErrorMessage(state.message)
    }
}