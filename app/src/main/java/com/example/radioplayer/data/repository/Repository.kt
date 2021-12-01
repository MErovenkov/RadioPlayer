package com.example.radioplayer.data.repository

import com.google.android.exoplayer2.MediaItem
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.FileNotFoundException

class Repository(private val radioLocalData: RadioLocalData) {

    private val mutex = Mutex()
    private var radioList: List<MediaItem> = emptyList()

    @Throws(FileNotFoundException::class)
    fun getRadioItemList(): Flow<List<MediaItem>> = radioLocalData.getRadioItemList()
        .map {
            mutex.withLock(radioList) {
                radioList = it
                radioList
            }
        }
        .flowOn(Dispatchers.IO)

    @Suppress("BlockingMethodInNonBlockingContext")
    fun getRadioItem(title: String): Flow<MediaItem?> = flow {
            if (radioList.isEmpty()) {
                getRadioItemList().collect()
            }

            val radioItem: MediaItem? = mutex.withLock(radioList) {
                radioList.firstOrNull { mediaItem -> mediaItem.mediaMetadata.title == title }
            }

            emit(radioItem)
        }
        .flowOn(Dispatchers.IO)
}