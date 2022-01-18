package com.example.radioplayer.data.repository.database.dao

import androidx.room.*
import com.example.radioplayer.data.repository.database.model.Radio
import com.example.radioplayer.data.repository.database.model.RadioWithFavoriteMusic
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RadioDao: GenericDao<Radio> {

    @Transaction
    @Query("SELECT * FROM table_radio WHERE title_radio = :radioTitle")
    abstract fun getRadio(radioTitle: String): Flow<RadioWithFavoriteMusic?>

    @Query("SELECT id_radio FROM table_radio WHERE title_radio =:radioTitle")
    abstract fun getRadioIdByTitle(radioTitle: String): Long

    @Query("SELECT * FROM table_radio")
    abstract fun getRadioList(): Flow<List<Radio>>

    @Query("DELETE FROM table_radio WHERE title_radio = :radioTitle")
    abstract fun deleteRadioByTitle(radioTitle: String)
}