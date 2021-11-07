package com.example.radioplayer.ui.exoplayer.service

import android.app.Notification
import com.google.android.exoplayer2.ui.PlayerNotificationManager

class RadioPlayerNotificationListener(private val musicService: RadioPlayerService)
    : PlayerNotificationManager.NotificationListener {

    private var isPlaying = false

    override fun onNotificationPosted(notificationId: Int,
                                      notification: Notification,
                                      ongoing: Boolean) {
        isPlaying = when (ongoing) {
            true -> {
                if (!isPlaying) musicService.play()
                true
            }
            false -> false
        }
    }

    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        super.onNotificationCancelled(notificationId, dismissedByUser)
        musicService.apply {
            stopForeground(true)
            stopSelf()
        }
    }
}