package com.example.radioplayer.ui.screen.player

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.radioplayer.ui.exoplayer.service.RadioPlayerService
import com.example.radioplayer.ui.exoplayer.service.RadioServiceBinder
import com.example.radioplayer.util.state.UiState
import com.example.radioplayer.ui.screen.player.view.RadioPlayerDisplay
import com.example.radioplayer.ui.screen.view.ErrorMessage
import com.example.radioplayer.ui.screen.view.Loading
import com.example.radioplayer.util.extension.foregroundStartService
import com.example.radioplayer.viewmodel.DetailRadioViewModel

@Composable
fun RadioPlayerScreen(title: String, detailRadioViewModel: DetailRadioViewModel) {
    val viewState = detailRadioViewModel.radioState.collectAsState()

    LaunchedEffect(title) {
        detailRadioViewModel.findRadioItem(title)
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
                }

                val intent = RadioPlayerService.getNewIntent(context, state.resources)

                context.foregroundStartService(intent)
                context.bindService(intent, connection, Context.BIND_AUTO_CREATE)

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