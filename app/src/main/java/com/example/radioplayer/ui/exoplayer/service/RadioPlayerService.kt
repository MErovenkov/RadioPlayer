package com.example.radioplayer.ui.exoplayer.service

import android.app.*
import android.content.Context
import android.os.IBinder
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
import android.os.Build
import androidx.core.app.NotificationCompat
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RadioPlayerService: Service() {
    companion object {
        const val NOTIFICATION_CHANNEL_NAME = "radioChannel"
        const val NOTIFICATION_CHANNEL_ID = "radioChannelId"
        private const val NOTIFICATION_ID = 1234

        private const val REQUEST_CODE = 0
        private const val ACTION_PLAY_KEY = "actionPlay"
        private const val ACTION_PAUSE_KEY = "actionPause"

        private const val TITLE_RADIO_KEY = "titleRadio"
        private const val URI_RADIO_KEY = "uriRadio"

        fun getNewIntent(context: Context, itemRadio: MediaItem): Intent {
            return Intent(context, RadioPlayerService::class.java)
                .apply {
                    putExtra(TITLE_RADIO_KEY, itemRadio.mediaMetadata.title)
                    putExtra(URI_RADIO_KEY, itemRadio.playbackProperties!!.uri.toString())
                }
        }
    }

    private lateinit var playAction: NotificationCompat.Action
    private lateinit var pauseAction: NotificationCompat.Action
    private lateinit var notificationBuilder: NotificationCompat.Builder

    private var exoPlayer: SimpleExoPlayer? = null

    private val _exoPlayerState: MutableStateFlow<PlayerState> = MutableStateFlow(PlayerState.Buffering())
    val exoPlayerState: StateFlow<PlayerState> = _exoPlayerState.asStateFlow()

    private val musicTitle: MutableState<String> = mutableStateOf(String())
    private var radioTitle: String = String()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        buildNotification()

        exoPlayer = SimpleExoPlayer.Builder(this).build()
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

                            this@RadioPlayerService.musicTitle.value = musicTitle
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
                    PlayerState.Playing(radioTitle, this@RadioPlayerService.musicTitle.value)
                false -> _exoPlayerState.value =
                    PlayerState.Pause(radioTitle, this@RadioPlayerService.musicTitle.value)
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID,
                                              NOTIFICATION_CHANNEL_NAME,
                                              NotificationManager.IMPORTANCE_LOW)

            (getSystemService(Application.NOTIFICATION_SERVICE)
                    as NotificationManager).createNotificationChannel(channel)
        }
    }
    private fun buildNotification() {
        playAction = createNotificationAction(R.drawable.ic_exo_play, ACTION_PLAY_KEY)
        pauseAction = createNotificationAction(R.drawable.ic_exo_pause, ACTION_PAUSE_KEY)
        notificationBuilder = NotificationCompat
            .Builder(this@RadioPlayerService, NOTIFICATION_CHANNEL_ID)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.ic_exo_play)
            .setStyle(androidx.media.app.NotificationCompat
                .MediaStyle()
                .setShowActionsInCompactView(0)
            )
            .setShowWhen(false)

        CoroutineScope(Dispatchers.IO).launch {
            _exoPlayerState.collect { state ->
                updateNotification(notificationBuilder, state)
            }
        }
    }

    private fun createNotificationAction(icon: Int, title: String): NotificationCompat.Action {
        val pendingIntent = PendingIntent
            .getService(this,
                REQUEST_CODE,
                Intent(this, this::class.java).apply { action = title },
                getFlagNotification()
            )

        return NotificationCompat.Action.Builder(icon, title, pendingIntent).build()
    }

    private fun getFlagNotification(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else PendingIntent.FLAG_UPDATE_CURRENT
    }

    private fun updateNotification(notificationBuilder: NotificationCompat.Builder,
                                   state: PlayerState) {
        with(notificationBuilder) {
            clearActions()
            setContentTitle(radioTitle)
            setContentText(
                when (state) {
                    is PlayerState.Buffering -> getString(R.string.loading_message)
                    is PlayerState.Error -> getString(state.message)
                    is PlayerState.Playing -> state.musicTitle
                    is PlayerState.Pause -> state.musicTitle
                }
            )
            addAction(
                when (state.isPlaying) {
                    true -> pauseAction
                    false -> playAction
                }
            )

            startForeground(NOTIFICATION_ID, build())

            if (state is PlayerState.Pause
                || state is PlayerState.Error) stopForeground(false)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.apply {
            when (intent.action) {
                ACTION_PLAY_KEY -> this@RadioPlayerService.play()
                ACTION_PAUSE_KEY -> this@RadioPlayerService.pause()
                else -> {
                    val newTitleRadio = getStringExtra(TITLE_RADIO_KEY).toString()

                    if (radioTitle != newTitleRadio) {
                        radioTitle = newTitleRadio

                        exoPlayer?.apply {
                            val uri = Uri.parse(getStringExtra(URI_RADIO_KEY).toString())

                            playWhenReady = true
                            setMediaItem(MediaItem.fromUri(uri))
                            prepare()
                        }
                    }
                }
            }
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder = RadioServiceBinder(this)

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer?.release()
        exoPlayer = null

        NotificationManagerCompat.from(this).cancelAll()
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