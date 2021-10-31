package com.example.radioplayer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.radioplayer.data.repository.Repository
import com.example.radioplayer.R
import com.example.radioplayer.util.state.UiState
import com.google.android.exoplayer2.MediaItem
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.FileNotFoundException

class RadioViewModel(private val repository: Repository): ViewModel() {
    private val _radioListState: MutableStateFlow<UiState<List<MediaItem>>>
        = MutableStateFlow(UiState.Loading)

    val radioListState: StateFlow<UiState<List<MediaItem>>> = _radioListState.asStateFlow()

    private val handler = CoroutineExceptionHandler { _, exception ->
        when(exception) {
            is FileNotFoundException ->
                _radioListState.value = UiState.Error(R.string.error_radio_json_not_found)
        }
    }

    init {
        CoroutineScope(viewModelScope.coroutineContext + handler).launch(Dispatchers.IO) {
            repository.getRadioItemList().collect {
                _radioListState.value = UiState.Success(it)
            }
        }
    }
}