package com.example.radioplayer.data.repository.database.model

import androidx.room.*
import com.example.radioplayer.data.util.DatabaseNaming.RadioEntry

@Entity(tableName = RadioEntry.TABLE_NAME,
        indices = [Index(value = [RadioEntry.COLUMN_TITLE], unique = true)])
data class Radio(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = RadioEntry.COLUMN_ID)
    val id: Long = 0,

    @ColumnInfo(name = RadioEntry.COLUMN_TITLE)
    val title: String,
)