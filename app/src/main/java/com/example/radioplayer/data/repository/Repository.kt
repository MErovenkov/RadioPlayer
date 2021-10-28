package com.example.radioplayer.data.repository

import com.google.android.exoplayer2.MediaItem
import kotlinx.coroutines.flow.Flow
import java.io.FileNotFoundException

class Repository(private val radioLocalData: RadioLocalData) {

    @Throws(FileNotFoundException::class)
    fun getRadioItemList(): Flow<List<MediaItem>> = radioLocalData.getRadioItemList()

    @Throws(FileNotFoundException::class)
    fun getRadioItem(title: String): Flow<MediaItem?> = radioLocalData.getRadioItem(title)
}