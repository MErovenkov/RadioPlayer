package com.example.radioplayer.data.repository.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.radioplayer.data.util.DatabaseNaming.FavoriteMusicEntry
import com.example.radioplayer.data.util.DatabaseNaming.RadioEntry

data class RadioWithFavoriteMusic(
    @Embedded
    val radio: Radio,

    @Relation(
        parentColumn = RadioEntry.COLUMN_ID,
        entityColumn = FavoriteMusicEntry.COLUMN_RADIO_ID,
        entity = FavoriteMusic::class
    )
    val favoriteMusicList: List<FavoriteMusic>
)