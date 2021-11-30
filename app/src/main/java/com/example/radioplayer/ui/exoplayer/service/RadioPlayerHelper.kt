package com.example.radioplayer.ui.exoplayer.service

import android.content.Context
import android.net.Uri
import com.example.radioplayer.R
import com.example.radioplayer.util.CheckStatusNetwork
import com.example.radioplayer.util.state.PlayerState
import com.google.android.exoplayer2.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RadioPlayerHelper(context: Context) {
    private var exoPlayer: SimpleExoPlayer? = null

    private val _exoPlayerState: MutableStateFlow<PlayerState> = MutableStateFlow(PlayerState.Buffering())
    val exoPlayerState: StateFlow<PlayerState> = _exoPlayerState.asStateFlow()

    private var radioTitle: String = String()
    private var musicTitle: String = String()

    init {
        exoPlayer = SimpleExoPlayer.Builder(context).build()
            .apply {
                addListener(
                    object : Player.Listener {
                        override fun onIsPlayingChanged(isPlaying: Boolean) {
                            super.onIsPlayingChanged(isPlaying)
                            updateExoPlayerState(playbackState, isPlaying)
                        }

                        override fun onPlaybackStateChanged(playbackState: Int) {
                            super.onPlaybackStateChanged(playbackState)

                            if (playbackState == Player.STATE_BUFFERING) {
                                _exoPlayerState.value = PlayerState.Buffering()
                            }
                        }

                        override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                            super.onMediaMetadataChanged(mediaMetadata)

                            val musicTitle = when (mediaMetadata.title.isNullOrBlank()) {
                                true -> ""
                                false -> mediaMetadata.title.toString()
                            }

                            this@RadioPlayerHelper.musicTitle = musicTitle
                            updateExoPlayerState(playbackState, isPlaying)
                        }

                        override fun onPlayerError(error: PlaybackException) {
                            super.onPlayerError(error)

                            val message = when (error.errorCode) {
                                PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> {
                                    if (!CheckStatusNetwork.isActive) R.string.error_no_internet_access
                                    else R.string.error_radio_station_unavailable
                                }
                                PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT ->
                                    R.string.error_radio_station_connection_timeout
                                PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND ->
                                    R.string.error_file_not_found
                                else -> R.string.error_unknown
                            }

                            _exoPlayerState.value = PlayerState.Error(message)
                        }
                    }
                )
            }
    }

    private fun updateExoPlayerState(playbackState: Int, isPlaying: Boolean) {
        if (playbackState == Player.STATE_READY) {
            when (isPlaying) {
                true -> _exoPlayerState.value =
                    PlayerState.Playing(radioTitle, musicTitle)
                false -> _exoPlayerState.value =
                    PlayerState.Pause(radioTitle, musicTitle)
            }
        }
    }

    fun setRadioItem(newTitleRadio: String, radioUri: Uri) {
        if (radioTitle != newTitleRadio) {
            radioTitle = newTitleRadio

            exoPlayer?.apply {
                playWhenReady = true
                setMediaItem(MediaItem.fromUri(radioUri))
                prepare()
            }
        }
    }

    fun play() {
        when (_exoPlayerState.value) {
            is PlayerState.Error -> exoPlayer?.prepare()
            else -> {
                exoPlayer?.apply {
                    seekToDefaultPosition()
                    play()
                }
            }
        }
    }

    fun pause() {
        exoPlayer?.pause()
    }

    fun release() {
        exoPlayer?.release()
        exoPlayer = null
    }
}