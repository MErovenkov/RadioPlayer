package com.example.radioplayer.data.util

object DatabaseNaming {
    const val DATABASE_NAME = "radio.database"

    object RadioEntry {
        const val TABLE_NAME = "table_radio"
        const val COLUMN_ID = "id_radio"
        const val COLUMN_TITLE = "title_radio"
    }

    object FavoriteMusicEntry {
        const val TABLE_NAME = "table_favorite_music"
        const val COLUMN_ID = "id_favorite_music"
        const val COLUMN_TITLE = "title_favorite_music"
        const val COLUMN_RADIO_ID = "radio_id"
    }
}