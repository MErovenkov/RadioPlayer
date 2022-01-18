package com.example.radioplayer.data.repository.database.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.example.radioplayer.data.util.DatabaseNaming.FavoriteMusicEntry
import com.example.radioplayer.data.util.DatabaseNaming.RadioEntry

@Entity(tableName = FavoriteMusicEntry.TABLE_NAME,
        foreignKeys = [ForeignKey(
            entity = Radio::class,
            parentColumns = arrayOf(RadioEntry.COLUMN_ID),
            childColumns = arrayOf(FavoriteMusicEntry.COLUMN_RADIO_ID),
            onDelete = CASCADE,
            onUpdate = CASCADE
        )],
        indices = [Index(value = [FavoriteMusicEntry.COLUMN_TITLE,
                                  FavoriteMusicEntry.COLUMN_RADIO_ID],
                         unique = true)]
)
class FavoriteMusic(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = FavoriteMusicEntry.COLUMN_ID)
    val id: Long = 0,

    @ColumnInfo(name = FavoriteMusicEntry.COLUMN_TITLE)
    val title: String,

    @ColumnInfo(name = FavoriteMusicEntry.COLUMN_RADIO_ID)
    val radioId: Long
)