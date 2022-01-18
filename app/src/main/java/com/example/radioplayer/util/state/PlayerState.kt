package com.example.radioplayer.util.state

import androidx.annotation.StringRes

sealed class PlayerState(val isPlaying: Boolean) {
    class Buffering: PlayerState(false)
    data class Playing(val radioTitle: String, val musicState: MusicState): PlayerState(true)
    data class Pause(val radioTitle: String, val musicState: MusicState): PlayerState(false)
    data class Stop(val radioTitle: String): PlayerState(false) {
        val musicState: MusicState = MusicState.Other(String())
    }
    data class Error(@StringRes val message: Int): PlayerState(false)

    fun getMusicStateValue(): MusicState? {
        return when (this) {
            is Buffering -> null
            is Playing -> musicState
            is Pause -> musicState
            is Stop -> null
            is Error -> null
        }
    }
}