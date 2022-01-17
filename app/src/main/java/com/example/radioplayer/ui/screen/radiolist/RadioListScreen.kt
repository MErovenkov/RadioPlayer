package com.example.radioplayer.ui.screen.radiolist

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import com.example.radioplayer.util.state.UiState
import com.example.radioplayer.ui.screen.radiolist.view.RadioDisplay
import com.example.radioplayer.ui.screen.view.ErrorMessage
import com.example.radioplayer.ui.screen.view.Loading
import com.example.radioplayer.viewmodel.RadioViewModel

@Composable
fun RadioListScreen (navController: NavHostController,
                     radioViewModel: RadioViewModel
) {
    val viewState = radioViewModel.radioListState.collectAsState()

    when (val state = viewState.value) {
        is UiState.Loading -> Loading()
        is UiState.Success -> {

            RadioDisplay(
                radioItemList = state.resources,
                onClick = { navigationState ->
                    navController
                        .navigate("${navigationState.route}/${navigationState.data}") {
                            popUpTo(navigationState.route)
                        }
                }
            )
        }
        is UiState.Error -> ErrorMessage(state.message)
    }
}