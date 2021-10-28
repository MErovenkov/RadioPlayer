package com.example.radioplayer.di.application

import android.content.Context
import com.example.radioplayer.data.repository.RadioLocalData
import com.example.radioplayer.data.repository.Repository
import com.example.radioplayer.data.util.Parser
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun repository(radioLocalData: RadioLocalData): Repository = Repository(radioLocalData)

    @Provides
    @Singleton
    fun radioLocalData(parser: Parser): RadioLocalData = RadioLocalData(parser)

    @Provides
    @Singleton
    fun parser(moshi: Moshi, @ApplicationContext context: Context): Parser = Parser(moshi, context)

    @Provides
    @Singleton
    fun moshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
}