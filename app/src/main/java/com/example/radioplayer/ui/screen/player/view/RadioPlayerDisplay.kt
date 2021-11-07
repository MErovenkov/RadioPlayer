package com.example.radioplayer.ui.screen.player.view

import android.content.*
import android.content.Context.BIND_AUTO_CREATE
import android.os.IBinder
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.radioplayer.ui.exoplayer.service.RadioPlayerService
import com.example.radioplayer.ui.exoplayer.service.RadioServiceBinder
import com.google.android.exoplayer2.*

@Composable
fun RadioPlayerDisplay(radioItem: MediaItem) {
    val context = LocalContext.current
    var radioPlayerService: RadioPlayerService? by remember { mutableStateOf(null) }

    DisposableEffect(radioItem) {
        val connection = object: ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as RadioServiceBinder
                radioPlayerService = binder.service
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                radioPlayerService = null
            }
        }

        val intent = RadioPlayerService.getNewIntent(context, radioItem)

        context.startService(intent)
        context.bindService(intent, connection, BIND_AUTO_CREATE)

        onDispose {
            context.unbindService(connection)
            context.stopService(intent)
        }
    }

    radioPlayerService?.let {
        val playerState = it.exoPlayerState.collectAsState()

        RadioPlayerControlView(playerState = playerState.value) {
            when (playerState.value.isPlaying) {
                true -> radioPlayerService?.pause()
                false -> radioPlayerService?.play()
            }
        }
    }
}