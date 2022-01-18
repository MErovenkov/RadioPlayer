package com.example.radioplayer.data.repository

import com.example.radioplayer.data.repository.database.IRadioDatabase
import com.example.radioplayer.data.repository.database.model.FavoriteMusic
import com.example.radioplayer.data.repository.database.model.Radio
import com.example.radioplayer.data.repository.database.model.RadioWithFavoriteMusic
import com.google.android.exoplayer2.MediaItem
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.FileNotFoundException

class Repository(private val radioJsonData: RadioJsonData,
                 private val radioDatabase: IRadioDatabase) {

    companion object {
        private const val NOT_FOUND_KEY = 0L
    }

    private val mutex = Mutex()
    private var radioList: List<MediaItem> = emptyList()

    // json data
    @Throws(FileNotFoundException::class)
    fun getRadioItemList(): Flow<List<MediaItem>> = flow {
            emit(mutex.withLock(radioList) {
                getRadioList()
            })
        }
        .flowOn(Dispatchers.IO)

    @Throws(FileNotFoundException::class)
    fun getRadioItem(radioTitle: String): Flow<MediaItem?> = flow {
            val radioItem: MediaItem? = mutex.withLock(radioList) {
                getRadioList()
                    .firstOrNull { mediaItem -> mediaItem.mediaMetadata.title == radioTitle }
            }

            emit(radioItem)
        }
        .flowOn(Dispatchers.IO)

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun getRadioList(): List<MediaItem> = withContext(Dispatchers.IO)  {
        if (radioList.isEmpty()) {
            radioList = radioJsonData.getRadioItemList().firstOrNull() ?: emptyList()
        }

        return@withContext radioList
    }
    // database
    private suspend fun createRadio(radioTitle: String): Long = withContext(Dispatchers.IO) {
        radioDatabase.insertRadio(Radio(title = radioTitle))
    }

    fun getRadio(radioTitle: String): Flow<RadioWithFavoriteMusic?> =
        radioDatabase.getRadio(radioTitle).flowOn(Dispatchers.IO)

    fun getRadioDateList(): Flow<List<Radio>> = radioDatabase.getRadioDateList()

    suspend fun deleteRadioByTitle(radioTitle: String) = withContext(Dispatchers.IO) {
        radioDatabase.deleteRadioByTitle(radioTitle)
    }

    suspend fun createFavoriteMusic(radioTitle: String,
                                    musicTitle: String) = withContext(Dispatchers.IO) {
        var radioId = radioDatabase.getRadioIdByTitle(radioTitle)

        if (radioId == NOT_FOUND_KEY) {
            radioId = createRadio(radioTitle)
        }
        radioDatabase.insertFavoriteMusic(FavoriteMusic(title = musicTitle, radioId = radioId))
    }

    fun getFavoriteMusic(radioTitle: String, musicTitle: String): Flow<FavoriteMusic?> =
        radioDatabase.getFavoriteMusic(radioTitle, musicTitle).flowOn(Dispatchers.IO)

    suspend fun deleteFavoriteMusic(favoriteMusic: FavoriteMusic) = withContext(Dispatchers.IO) {
        radioDatabase.deleteFavoriteMusic(favoriteMusic)
    }

    suspend fun deleteFavoriteMusicByTitle(radioTitle: String,
                                           musicTitle: String) = withContext(Dispatchers.IO)  {
        val radioId = radioDatabase.getRadioIdByTitle(radioTitle)

        if (radioId != NOT_FOUND_KEY) {
            radioDatabase.deleteFavoriteMusicByTitle(radioId, musicTitle)
        }
    }
}