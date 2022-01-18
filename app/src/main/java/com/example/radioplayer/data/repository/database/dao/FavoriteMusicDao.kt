package com.example.radioplayer.data.repository.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.example.radioplayer.data.repository.database.model.FavoriteMusic
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMusicDao: GenericDao<FavoriteMusic> {

    @Query("SELECT * FROM table_favorite_music " +
            "INNER JOIN table_radio " +
            "ON table_favorite_music.radio_id = table_radio.id_radio " +
            "WHERE table_radio.title_radio = :radioTitle " +
            "AND table_favorite_music.title_favorite_music = :musicTitle")
    fun getFavoriteMusic(radioTitle: String, musicTitle: String): Flow<FavoriteMusic?>

    @Delete
    fun delete(favoriteMusic: FavoriteMusic): Int

    @Query("DELETE FROM table_favorite_music WHERE radio_id = :radioId AND title_favorite_music = :musicTitle")
    fun deleteFavoriteMusicByTitle(radioId: Long, musicTitle: String)
}