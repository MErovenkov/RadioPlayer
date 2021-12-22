package com.example.radioplayer.data.repository

import com.example.radioplayer.data.dto.RadioDto
import com.example.radioplayer.data.util.Parser
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException

@Suppress("BlockingMethodInNonBlockingContext")
class RadioLocalData(private val parser: Parser) {

    companion object {
        private const val NAME_MEDIA_JSON_FILE = "radio.json"
    }

    @Throws(FileNotFoundException::class)
    fun getRadioItemList(): Flow<List<MediaItem>> = flow {
        val radioDtoList = withContext(Dispatchers.IO) {
            parser.getRadioDtoList(NAME_MEDIA_JSON_FILE)
        }

        val radioItemList = radioDtoList
            ?.filter { radioDto -> radioDto.uri != null }
            ?.map { radioDto -> buildRadioItem(radioDto) } as List<MediaItem>

        emit(radioItemList)
    }

    private fun buildRadioItem(radioDto: RadioDto): MediaItem {
        return MediaItem.Builder()
            .setUri(radioDto.uri)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(radioDto.title)
                    .build()
            )
            .build()
    }
}