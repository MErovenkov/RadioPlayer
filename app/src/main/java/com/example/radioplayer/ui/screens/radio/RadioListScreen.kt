package com.example.radioplayer.ui.screens.radio

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import com.example.radioplayer.util.state.UiState
import com.example.radioplayer.ui.screens.radio.views.RadioListDisplay
import com.example.radioplayer.ui.screens.component.ErrorMessage
import com.example.radioplayer.ui.screens.component.Loading
import com.example.radioplayer.viewmodel.RadioViewModel

@Composable
fun RadioListScreen (
    navController: NavHostController,
    radioViewModel: RadioViewModel
) {
    val viewState = radioViewModel.radioListState.collectAsState()

    when (val state = viewState.value) {
        is UiState.Loading -> Loading()
        is UiState.Success -> RadioListDisplay(navController, state.resources)
        is UiState.Error -> ErrorMessage(state.message)
    }
}