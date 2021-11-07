package com.example.radioplayer.ui.exoplayer.service

import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator

class RadioTimelineQueue(mediaSessionCompat: MediaSessionCompat): TimelineQueueNavigator(mediaSessionCompat) {
    override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
        return MediaDescriptionCompat.Builder()
            .setMediaUri(player.currentMediaItem?.playbackProperties?.uri).build()
    }
}