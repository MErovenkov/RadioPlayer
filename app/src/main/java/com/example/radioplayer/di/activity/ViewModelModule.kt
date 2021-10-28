package com.example.radioplayer.di.activity

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.radioplayer.data.repository.Repository
import com.example.radioplayer.viewmodel.DetailRadioViewModel
import com.example.radioplayer.viewmodel.RadioViewModel
import com.example.radioplayer.viewmodel.ViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {

    @Provides
    @ActivityScope
    fun viewModelFactory(repository: Repository): ViewModelFactory = ViewModelFactory(repository)

    @Provides
    @ActivityScope
    fun radioViewModel(@ActivityContext context: Context, viewModelFactory: ViewModelFactory
    ): RadioViewModel = ViewModelProvider(context as ViewModelStoreOwner,
        viewModelFactory)[RadioViewModel::class.java]

    @Provides
    @ActivityScope
    fun detailRadioViewModel(@ActivityContext context: Context, viewModelFactory: ViewModelFactory
    ): DetailRadioViewModel = ViewModelProvider(context as ViewModelStoreOwner,
        viewModelFactory)[DetailRadioViewModel::class.java]
}