package com.example.radioplayer.ui.exoplayer.service

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.radioplayer.R
import com.example.radioplayer.util.CheckStatusNetwork
import com.example.radioplayer.util.state.PlayerState
import com.google.android.exoplayer2.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RadioPlayerService: Service() {
    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "radioChannel"
        private const val NOTIFICATION_ID = 1

        private const val MEDIA_SESSION_TAG = "radioSession"

        private const val TITLE_RADIO_KEY = "titleRadio"
        private const val URI_RADIO_KEY = "uriRadio"

        fun getNewIntent(context: Context, titleRadio: MediaItem): Intent {
            return Intent(context, RadioPlayerService::class.java)
                .apply {
                    putExtra(TITLE_RADIO_KEY, titleRadio.mediaMetadata.title)
                    putExtra(URI_RADIO_KEY, titleRadio.playbackProperties!!.uri.toString())
                }
        }
    }

    private var exoPlayer: SimpleExoPlayer? = null
    private var playerNotificationManager: PlayerNotificationManager? = null
    private var mediaSessionCompat: MediaSessionCompat? = null
    private var mediaSessionConnector: MediaSessionConnector? = null

    private val _exoPlayerState: MutableStateFlow<PlayerState> = MutableStateFlow(PlayerState.Buffering())
    val exoPlayerState: StateFlow<PlayerState> = _exoPlayerState.asStateFlow()

    private val isPlaying: MutableState<Boolean> = mutableStateOf(false)
    private val playbackState: MutableState<Int> = mutableStateOf(0)

    private lateinit var radioTitle: String

    override fun onCreate() {
        super.onCreate()

        exoPlayer = SimpleExoPlayer.Builder(this).build()
            .apply {
                addListener(
                    object : Player.Listener {
                        override fun onIsPlayingChanged(isPlaying: Boolean) {
                            super.onIsPlayingChanged(isPlaying)
                            this@RadioPlayerService.isPlaying.value = isPlaying
                        }

                        override fun onPlaybackStateChanged(playbackState: Int) {
                            super.onPlaybackStateChanged(playbackState)
                            this@RadioPlayerService.playbackState.value = playbackState
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
        
        playerNotificationManager = PlayerNotificationManager
            .Builder(this, NOTIFICATION_ID, NOTIFICATION_CHANNEL_ID)
            .setChannelNameResourceId(R.string.app_name)
            .setMediaDescriptionAdapter(
                object: PlayerNotificationManager.MediaDescriptionAdapter {
                    override fun getCurrentContentTitle(player: Player): CharSequence = radioTitle

                    override fun getCurrentContentText(player: Player): CharSequence {
                        val musicTitle = when (player.mediaMetadata.title.isNullOrBlank()) {
                            true -> ""
                            false -> player.mediaMetadata.title.toString()
                        }

                        if (playbackState.value == Player.STATE_READY) {
                            when (isPlaying.value) {
                                true -> _exoPlayerState.value =
                                    PlayerState.Playing(radioTitle, musicTitle)
                                false -> _exoPlayerState.value =
                                    PlayerState.Pause(radioTitle, musicTitle)
                            }
                        } else if (playbackState.value == Player.STATE_BUFFERING) {
                            _exoPlayerState.value = PlayerState.Buffering()
                        }

                        return musicTitle
                    }

                    override fun getCurrentLargeIcon(
                        player: Player,
                        callback: PlayerNotificationManager.BitmapCallback): Bitmap? = null
                    override fun createCurrentContentIntent(player: Player): PendingIntent? = null
                }
            )
            .setSmallIconResourceId(R.drawable.exo_icon_play)
            .setNotificationListener(RadioPlayerNotificationListener(this))
            .build()
            .apply {
                setUsePreviousAction(false)
                setUseChronometer(false)
                setPlayer(exoPlayer)
            }

        mediaSessionCompat = MediaSessionCompat(applicationContext, MEDIA_SESSION_TAG)
            .apply {
                isActive = true
            }

        playerNotificationManager?.setMediaSessionToken(mediaSessionCompat!!.sessionToken)

        mediaSessionConnector = MediaSessionConnector(mediaSessionCompat!!)
            .apply {
                setQueueNavigator(RadioTimelineQueue(mediaSession))
                setPlayer(exoPlayer)
            }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.apply {
            radioTitle = getStringExtra(TITLE_RADIO_KEY).toString()

            exoPlayer?.apply {
                val uri = Uri.parse(getStringExtra(URI_RADIO_KEY).toString())

                playWhenReady = true
                setMediaItem(MediaItem.fromUri(uri))
                prepare()
            }
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder = RadioServiceBinder(this)

    override fun onDestroy() {
        super.onDestroy()
        mediaSessionCompat?.release()
        mediaSessionCompat = null

        mediaSessionConnector?.setPlayer(null)
        mediaSessionConnector = null

        playerNotificationManager?.setPlayer(null)
        playerNotificationManager = null

        exoPlayer?.release()
        exoPlayer = null
    }

    fun pause() {
        exoPlayer?.pause()
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
}