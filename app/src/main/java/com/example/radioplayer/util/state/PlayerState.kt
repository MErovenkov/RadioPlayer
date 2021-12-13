package com.example.radioplayer.util.state

import androidx.annotation.StringRes

sealed class PlayerState(val isPlaying: Boolean) {
    class Buffering: PlayerState(false)
    data class Playing(val radioTitle: String, val musicTitle: String): PlayerState(true)
    data class Pause(val radioTitle: String, val musicTitle: String): PlayerState(false)
    data class Stop(val radioTitle: String): PlayerState(false) {
        val musicTitle: String = String()
    }
    data class Error(@StringRes val message: Int): PlayerState(false)
}