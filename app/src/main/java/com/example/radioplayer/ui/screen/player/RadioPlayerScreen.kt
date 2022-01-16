package com.example.radioplayer.ui.screen.player

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.radioplayer.ui.exoplayer.service.RadioPlayerService
import com.example.radioplayer.ui.exoplayer.service.RadioServiceBinder
import com.example.radioplayer.util.state.UiState
import com.example.radioplayer.ui.screen.player.view.RadioPlayerDisplay
import com.example.radioplayer.ui.screen.view.ErrorMessage
import com.example.radioplayer.ui.screen.view.Loading
import com.example.radioplayer.util.extension.foregroundStartService
import com.example.radioplayer.viewmodel.RadioPlayerViewModel

@Composable
fun RadioPlayerScreen(navController: NavHostController,
                      radioTitle: String,
                      radioPlayerViewModel: RadioPlayerViewModel
) {
    val viewState = radioPlayerViewModel.radioState.collectAsState()

    LaunchedEffect(radioTitle) {
        radioPlayerViewModel.findRadioItem(radioTitle)
    }

    when (val state = viewState.value) {
        is UiState.Loading -> Loading()
        is UiState.Success -> {
            val context = LocalContext.current
            var radioPlayerService: RadioPlayerService? by remember { mutableStateOf(null) }

            DisposableEffect(state.resources) {
                val connection = object: ServiceConnection {
                    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                        val binder = service as RadioServiceBinder
                        radioPlayerService = binder.service
                    }

                    override fun onServiceDisconnected(name: ComponentName?) {
                        radioPlayerService = null
                    }

                    override fun onNullBinding(name: ComponentName?) {
                        navController.navigateUp()
                        super.onNullBinding(name)
                    }
                }

                val intent = RadioPlayerService.getNewIntent(context, state.resources)

                context.foregroundStartService(intent)
                context.bindService(intent, connection, 0)

                onDispose {
                    context.unbindService(connection)
                }
            }

            radioPlayerService?.let {
                val playerState = it.exoPlayerState.collectAsState()

                RadioPlayerDisplay(playerState.value) { isPlaying ->
                    when (isPlaying) {
                        true -> radioPlayerService?.play()
                        false -> radioPlayerService?.pause()
                    }
                }
            }
        }
        is UiState.Error -> ErrorMessage(state.message)
    }
}