package com.example.radioplayer.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class RadioDto (
    @Json(name = "title")
    val title: String = DEFAULT_RADIO_TITLE,

    @Json(name = "uri")
    val uri: String? = null,
) {
    companion object {
        private const val DEFAULT_RADIO_TITLE = "RADIO_TITLE"
    }
}