package com.example.radioplayer.data.repository.database.dao

import androidx.room.*

@Dao
interface GenericDao<T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: T): Long
}