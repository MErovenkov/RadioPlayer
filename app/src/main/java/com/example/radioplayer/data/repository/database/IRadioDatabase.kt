package com.example.radioplayer.data.repository.database

import com.example.radioplayer.data.repository.database.model.FavoriteMusic
import com.example.radioplayer.data.repository.database.model.Radio
import com.example.radioplayer.data.repository.database.model.RadioWithFavoriteMusic
import kotlinx.coroutines.flow.Flow

interface IRadioDatabase {

    suspend fun insertRadio(radio: Radio): Long

    fun getRadio(radioTitle: String): Flow<RadioWithFavoriteMusic?>

    suspend fun getRadioIdByTitle(radioTitle: String): Long

    fun getRadioDateList(): Flow<List<Radio>>

    suspend fun deleteRadioByTitle(radioTitle: String)

    suspend fun insertFavoriteMusic(favoriteMusic: FavoriteMusic): Long

    fun getFavoriteMusic(radioTitle: String, musicTitle: String): Flow<FavoriteMusic?>

    suspend fun deleteFavoriteMusic(favoriteMusic: FavoriteMusic)

    suspend fun deleteFavoriteMusicByTitle(radioId: Long, musicTitle: String)
}