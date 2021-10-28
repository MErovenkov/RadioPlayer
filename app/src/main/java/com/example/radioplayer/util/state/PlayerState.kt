package com.example.radioplayer.util.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class PlayerState(val isPlaying: MutableState<Boolean> = mutableStateOf(false),
                       val playbackState: MutableState<Int> = mutableStateOf(0),
                       val errorCode: MutableState<Int> = mutableStateOf(0))