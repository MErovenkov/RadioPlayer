package com.example.radioplayer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.radioplayer.R
import com.example.radioplayer.data.repository.Repository
import com.example.radioplayer.util.state.UiState
import com.google.android.exoplayer2.MediaItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailRadioViewModel(private val repository: Repository): ViewModel() {
    private val _radioState: MutableStateFlow<UiState<MediaItem>>
            = MutableStateFlow(UiState.Loading)

    val radioState: StateFlow<UiState<MediaItem>> = _radioState.asStateFlow()

    fun findRadioItem(title: String) {
        CoroutineScope(viewModelScope.coroutineContext).launch(Dispatchers.IO) {
            repository.getRadioItem(title).collect {
                _radioState.value = it?.let {
                    UiState.Success(it)
                } ?: UiState.Error(R.string.error_radio_by_title_not_found)
            }
        }
    }
}