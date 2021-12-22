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
    fun getRadioItemList(): Flow<List<MediaItem>> = flow {
            emit(mutex.withLock(radioList) {
                getRadioList()
            })
        }
        .flowOn(Dispatchers.IO)

    @Throws(FileNotFoundException::class)
    fun getRadioItem(title: String): Flow<MediaItem?> = flow {
            val radioItem: MediaItem? = mutex.withLock(radioList) {
                getRadioList().firstOrNull { mediaItem -> mediaItem.mediaMetadata.title == title }
            }

            emit(radioItem)
        }
        .flowOn(Dispatchers.IO)

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun getRadioList(): List<MediaItem> = withContext(Dispatchers.IO)  {
        if (radioList.isEmpty()) {
            radioList = radioLocalData.getRadioItemList().firstOrNull() ?: emptyList()
        }

        return@withContext radioList
    }
}