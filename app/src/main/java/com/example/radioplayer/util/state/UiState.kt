package com.example.radioplayer.util.state

import androidx.annotation.StringRes

sealed class UiState<out T> {
    object Loading: UiState<Nothing>()
    data class Success<out T>(val resources: T): UiState<T>()
    data class Error(@StringRes val message: Int): UiState<Nothing>()
}