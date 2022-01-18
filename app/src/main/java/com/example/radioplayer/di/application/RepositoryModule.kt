package com.example.radioplayer.di.application

import android.content.Context
import androidx.room.Room
import com.example.radioplayer.data.repository.RadioJsonData
import com.example.radioplayer.data.repository.Repository
import com.example.radioplayer.data.repository.database.IRadioDatabase
import com.example.radioplayer.data.repository.database.RadioDatabase
import com.example.radioplayer.data.util.DatabaseNaming
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
    fun repository(radioJsonData: RadioJsonData, radioDatabase: IRadioDatabase)
        : Repository = Repository(radioJsonData, radioDatabase)

    @Provides
    @Singleton
    fun radioLocalData(parser: Parser): RadioJsonData = RadioJsonData(parser)

    @Provides
    @Singleton
    fun parser(moshi: Moshi, @ApplicationContext context: Context): Parser = Parser(moshi, context)

    @Provides
    @Singleton
    fun moshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    fun radioDatabase(@ApplicationContext context: Context): IRadioDatabase = Room
        .databaseBuilder(context, RadioDatabase::class.java, DatabaseNaming.DATABASE_NAME).build()
}