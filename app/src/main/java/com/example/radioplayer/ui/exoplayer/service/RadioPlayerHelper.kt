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
import android.media.AudioManager
import androidx.media.AudioAttributesCompat
import androidx.media.AudioFocusRequestCompat
import androidx.media.AudioManagerCompat

class RadioPlayerHelper(context: Context) {
    private var exoPlayer: SimpleExoPlayer? = null

    private val _exoPlayerState: MutableStateFlow<PlayerState>
            = MutableStateFlow(PlayerState.Buffering())

    val exoPlayerState: StateFlow<PlayerState> = _exoPlayerState.asStateFlow()

    private var radioTitle: String = String()
    private var musicTitle: String = String()

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val audioAttributes: AudioAttributesCompat = AudioAttributesCompat
        .Builder()
        .setUsage(AudioAttributesCompat.USAGE_MEDIA)
        .setContentType(AudioAttributesCompat.CONTENT_TYPE_MUSIC)
        .build()

    private var audioFocusState: Int = AudioManager.AUDIOFOCUS_GAIN

    private val audioFocusRequest: AudioFocusRequestCompat = AudioFocusRequestCompat
        .Builder(AudioManagerCompat.AUDIOFOCUS_GAIN)
        .setAudioAttributes(audioAttributes)
        .setWillPauseWhenDucked(true)
        .setOnAudioFocusChangeListener { audioFocusChange ->
            when (audioFocusChange) {
                AudioManager.AUDIOFOCUS_LOSS -> this.pause()
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> exoPlayer?.pause()
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> exoPlayer?.volume = 0.3f

                AudioManager.AUDIOFOCUS_GAIN -> {
                    when (this.audioFocusState) {
                        AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> this.play()
                        AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> exoPlayer?.volume = 1f
                    }
                }
            }

            this.audioFocusState = audioFocusChange
        }
        .build()

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
                                true -> String()
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
                setMediaItem(MediaItem.fromUri(radioUri))
                playWhenReady = true
                prepare()

                when(AudioManagerCompat.requestAudioFocus(audioManager, audioFocusRequest)) {
                    AudioManager.AUDIOFOCUS_REQUEST_FAILED -> {
                        stop()
                        _exoPlayerState.value = PlayerState.Stop(radioTitle)
                    }
                }
            }
        }
    }

    fun play() {
        when(AudioManagerCompat.requestAudioFocus(audioManager, audioFocusRequest)) {
            AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> {
                when (_exoPlayerState.value) {
                    is PlayerState.Error -> exoPlayer?.prepare()
                    is PlayerState.Stop -> exoPlayer?.prepare()
                    else -> {
                        exoPlayer?.apply {
                            seekToDefaultPosition()
                            play()
                        }
                    }
                }
            }
        }
    }

    fun pause() {
        AudioManagerCompat.abandonAudioFocusRequest(audioManager, audioFocusRequest)
        exoPlayer?.pause()
    }

    fun release() {
        AudioManagerCompat.abandonAudioFocusRequest(audioManager, audioFocusRequest)
        exoPlayer?.release()
        exoPlayer = null
    }
}