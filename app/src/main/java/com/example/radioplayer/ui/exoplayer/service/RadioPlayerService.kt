package com.example.radioplayer.ui.exoplayer.service

import android.app.*
import android.content.Context
import android.os.IBinder
import android.net.Uri
import com.example.radioplayer.R
import com.example.radioplayer.util.state.PlayerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat as MediaNotificationCompat
import android.content.Intent
import com.example.radioplayer.ui.MainActivity
import com.google.android.exoplayer2.MediaItem
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class RadioPlayerService: Service() {
    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "radioChannelId"
        private const val NOTIFICATION_ID = 1234

        private const val REQUEST_CODE = 0
        private const val ACTION_INIT_KEY = "actionInit"
        private const val ACTION_PLAY_KEY = "actionPlay"
        private const val ACTION_PAUSE_KEY = "actionPause"
        private const val ACTION_CLOSE_KEY = "actionClose"
        private const val ACTION_TAP_NOTIFICATION_KEY = "actionTapNotification"

        private const val TITLE_RADIO_KEY = "titleRadio"
        private const val URI_RADIO_KEY = "uriRadio"

        fun getNewIntent(context: Context, itemRadio: MediaItem): Intent {
            return Intent(context, RadioPlayerService::class.java)
                .apply {
                    putExtra(TITLE_RADIO_KEY, itemRadio.mediaMetadata.title)
                    putExtra(URI_RADIO_KEY, itemRadio.playbackProperties!!.uri.toString())
                    action = ACTION_INIT_KEY
                }
        }
    }

    private var radioPlayerHelper: RadioPlayerHelper? = null

    private lateinit var playAction: NotificationCompat.Action
    private lateinit var pauseAction: NotificationCompat.Action
    private lateinit var closeAction: NotificationCompat.Action

    private lateinit var notificationBuilder: NotificationCompat.Builder

    private val serviceJob: Job = CoroutineScope(Dispatchers.IO)
        .launch(start = CoroutineStart.LAZY) {
            radioPlayerHelper?.exoPlayerState?.collect { playerState ->
                _exoPlayerState.value = playerState
                updateNotification(notificationBuilder, playerState)
            }
        }

    private val _exoPlayerState: MutableStateFlow<PlayerState> = MutableStateFlow(PlayerState.Buffering())
    val exoPlayerState: StateFlow<PlayerState> = _exoPlayerState.asStateFlow()

    private var contentTitle: String = String()

    override fun onCreate() {
        super.onCreate()
        closeAction = createNotificationAction(R.drawable.ic_notification_close, ACTION_CLOSE_KEY)

        createNotificationChannel()

        notificationBuilder = createNotificationBuilder()

        playAction = createNotificationAction(R.drawable.ic_exo_play, ACTION_PLAY_KEY)
        pauseAction = createNotificationAction(R.drawable.ic_exo_pause, ACTION_PAUSE_KEY)

        radioPlayerHelper = RadioPlayerHelper(this)

        serviceJob.start()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID,
                                              this.resources.getString(R.string.app_name),
                                              NotificationManager.IMPORTANCE_LOW)

            (getSystemService(Application.NOTIFICATION_SERVICE)
                    as NotificationManager).createNotificationChannel(channel)
        }
    }

    private fun createNotificationAction(icon: Int, title: String): NotificationCompat.Action {
        return NotificationCompat.Action
            .Builder(icon, title, createServicePendingIntent(title))
            .build()
    }

    private fun createServicePendingIntent(action: String): PendingIntent {
        return PendingIntent.getService(this, REQUEST_CODE,
                Intent(this, this::class.java).apply { this.action = action },
                getFlagNotification())
    }

    private fun getFlagNotification(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else PendingIntent.FLAG_UPDATE_CURRENT
    }

    private fun createNotificationBuilder(): NotificationCompat.Builder {
        return NotificationCompat
            .Builder(this, NOTIFICATION_CHANNEL_ID)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.ic_exo_play)
            .setStyle(MediaNotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0, 1))
            .setContentIntent(createServicePendingIntent(ACTION_TAP_NOTIFICATION_KEY))
            .setDeleteIntent(createServicePendingIntent(ACTION_CLOSE_KEY))
            .setShowWhen(false)
    }

    private fun updateNotification(notificationBuilder: NotificationCompat.Builder,
                                   playerState: PlayerState) {

        val contentText = when (playerState) {
            is PlayerState.Buffering -> getString(R.string.loading_message)
            is PlayerState.Error -> getString(playerState.message)
            is PlayerState.Playing -> playerState.musicTitle
            is PlayerState.Pause -> playerState.musicTitle
            is PlayerState.Stop -> playerState.musicTitle
        }

        with(notificationBuilder) {
            clearActions()
            setContentText(contentText)

            addAction(closeAction)
            addAction(
                when (playerState.isPlaying) {
                    true -> pauseAction
                    false -> playAction
                }
            )

            startForeground(NOTIFICATION_ID, build())
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.apply {
            when (intent.action) {
                ACTION_PLAY_KEY -> this@RadioPlayerService.play()
                ACTION_PAUSE_KEY -> this@RadioPlayerService.pause()
                ACTION_CLOSE_KEY -> stopSelf(startId)
                ACTION_TAP_NOTIFICATION_KEY -> {
                    startActivity(MainActivity
                        .getNewIntent(this@RadioPlayerService, contentTitle))
                }
                ACTION_INIT_KEY -> {
                    contentTitle = getStringExtra(TITLE_RADIO_KEY).toString()
                    notificationBuilder.setContentTitle(contentTitle)

                    radioPlayerHelper?.setRadioItem(contentTitle,
                        Uri.parse(getStringExtra(URI_RADIO_KEY).toString()))
                }
            }
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder = RadioServiceBinder(this)

    fun play() {
        radioPlayerHelper?.play()
    }

    fun pause() {
        radioPlayerHelper?.pause()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()

        serviceJob.cancel()

        radioPlayerHelper?.release()
        radioPlayerHelper = null
    }
}