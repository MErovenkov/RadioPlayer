package com.example.radioplayer.ui.screen.player.view

import android.view.WindowManager
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.radioplayer.ui.MainActivity
import com.example.radioplayer.util.state.PlayerState
import com.google.android.exoplayer2.*

@Composable
fun RadioPlayerDisplay(radioItem: MediaItem) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val playerState: PlayerState = remember { PlayerState() }

    val exoPlayer = remember {
        SimpleExoPlayer.Builder(context).build()
            .apply {
                addListener(
                object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        super.onIsPlayingChanged(isPlaying)
                        playerState.isPlaying.value = isPlaying
                    }

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        super.onPlaybackStateChanged(playbackState)
                        playerState.playbackState.value = playbackState
                    }

                    override fun onPlayerError(error: PlaybackException) {
                        super.onPlayerError(error)
                        playerState.errorCode.value = error.errorCode
                    }
                })
            }
    }

    LaunchedEffect(radioItem) {
        exoPlayer.setMediaItem(radioItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    DisposableEffect(exoPlayer) {
        val window = (context as MainActivity).window
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    exoPlayer.seekToDefaultPosition()
                    exoPlayer.play()
                }
                Lifecycle.Event.ON_STOP -> exoPlayer.pause()
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
            exoPlayer.release()

            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    RadioPlayerControlView(
        radioItem.mediaMetadata.title.toString(),
        playerState
    ) {
        when (exoPlayer.isPlaying) {
            true -> exoPlayer.pause()
            false -> {
                if (playerState.errorCode.value != 0) {
                    exoPlayer.prepare()
                    playerState.errorCode.value = 0
                } else {
                    exoPlayer.seekToDefaultPosition()
                    exoPlayer.play()
                }
            }
        }
    }
}