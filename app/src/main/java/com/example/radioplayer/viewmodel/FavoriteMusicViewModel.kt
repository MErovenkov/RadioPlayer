package com.example.radioplayer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.radioplayer.R
import com.example.radioplayer.data.repository.Repository
import com.example.radioplayer.data.repository.database.model.FavoriteMusic
import com.example.radioplayer.data.repository.database.model.RadioWithFavoriteMusic
import com.example.radioplayer.util.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavoriteMusicViewModel(private val repository: Repository): ViewModel() {
    private val _favoriteMusicState: MutableStateFlow<UiState<RadioWithFavoriteMusic>>
            = MutableStateFlow(UiState.Loading)

    val favoriteMusicState: StateFlow<UiState<RadioWithFavoriteMusic>> = _favoriteMusicState.asStateFlow()

    fun findRadio(radioTitle: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getRadio(radioTitle).collect { radio ->

                _favoriteMusicState.value = radio?.let {
                    if (radio.favoriteMusicList.isEmpty()) {
                        UiState.Error(R.string.error_favorite_music_list_is_empty)
                    } else UiState.Success(radio)
                } ?: UiState.Error(R.string.error_favorite_music_list_is_empty)
            }
        }
    }

    fun deleteFavoriteMusic(favoriteMusic: FavoriteMusic) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFavoriteMusic(favoriteMusic)
        }
    }
}