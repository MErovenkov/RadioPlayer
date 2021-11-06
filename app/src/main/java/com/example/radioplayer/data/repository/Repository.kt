package com.example.radioplayer.data.repository

import com.google.android.exoplayer2.MediaItem
import kotlinx.coroutines.flow.*
import java.io.FileNotFoundException

class Repository(private val radioLocalData: RadioLocalData) {

    private var radioList: List<MediaItem> = listOf()

    @Throws(FileNotFoundException::class)
    fun getRadioItemList(): Flow<List<MediaItem>> = radioLocalData.getRadioItemList()
        .map {
            radioList = it
            radioList
        }

    fun getRadioItem(title: String): Flow<MediaItem?> = flow {
        emit(radioList.first{ mediaItem -> mediaItem.mediaMetadata.title == title})
    }
}