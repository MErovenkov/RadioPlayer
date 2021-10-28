package com.example.radioplayer.data.util

import android.content.Context
import com.example.radioplayer.data.dto.RadioDto
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.FileNotFoundException

class Parser(moshi: Moshi, context: Context) {
    private val applicationContext = context.applicationContext

    private val listTypes = Types.newParameterizedType(List::class.java, RadioDto::class.java)
    private val parserAdapter: JsonAdapter<List<RadioDto>> = moshi.adapter(listTypes)

    @Throws(FileNotFoundException::class)
    fun getRadioDto(nameFile: String, title: String): RadioDto? {
        return getRadioDtoList(nameFile)?.first { it -> it.title == title }
    }

    @Throws(FileNotFoundException::class)
    fun getRadioDtoList(nameFile: String): List<RadioDto>? {
        return parserAdapter.fromJson(readJsonFile(nameFile))
    }

    @Throws(FileNotFoundException::class)
    private fun readJsonFile(nameFile: String): String {
        var json: String

        applicationContext.assets.open(nameFile)
            .bufferedReader()
            .use { json = it.readText() }

        return json
    }
}