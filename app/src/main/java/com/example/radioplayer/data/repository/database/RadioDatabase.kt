package com.example.radioplayer.data.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.radioplayer.data.repository.database.dao.FavoriteMusicDao
import com.example.radioplayer.data.repository.database.dao.RadioDao
import com.example.radioplayer.data.repository.database.model.FavoriteMusic
import com.example.radioplayer.data.repository.database.model.Radio
import com.example.radioplayer.data.repository.database.model.RadioWithFavoriteMusic
import kotlinx.coroutines.flow.Flow

@Database(entities = [Radio::class, FavoriteMusic::class], version = 1)
abstract class RadioDatabase: RoomDatabase(), IRadioDatabase {
    abstract fun radioDao(): RadioDao
    abstract fun favoriteMusicDao(): FavoriteMusicDao

    override suspend fun insertRadio(radio: Radio): Long {
        return radioDao().insert(radio)
    }

    override fun getRadio(radioTitle: String): Flow<RadioWithFavoriteMusic?> {
        return radioDao().getRadio(radioTitle)
    }

    override suspend fun getRadioIdByTitle(radioTitle: String): Long {
        return radioDao().getRadioIdByTitle(radioTitle)
    }

    override fun getRadioDateList(): Flow<List<Radio>> {
        return radioDao().getRadioList()
    }

    override suspend fun deleteRadioByTitle(radioTitle: String) {
        return radioDao().deleteRadioByTitle(radioTitle)
    }

    override suspend fun insertFavoriteMusic(favoriteMusic: FavoriteMusic): Long {
        return favoriteMusicDao().insert(favoriteMusic)
    }

    override fun getFavoriteMusic(radioTitle: String, musicTitle: String): Flow<FavoriteMusic?> {
        return favoriteMusicDao().getFavoriteMusic(radioTitle, musicTitle)
    }

    override suspend fun deleteFavoriteMusic(favoriteMusic: FavoriteMusic) {
        favoriteMusicDao().delete(favoriteMusic)
    }

    override suspend fun deleteFavoriteMusicByTitle(radioId: Long, musicTitle: String) {
        favoriteMusicDao().deleteFavoriteMusicByTitle(radioId, musicTitle)
    }
}