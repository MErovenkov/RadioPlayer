package com.example.radioplayer.di.screen

import com.example.radioplayer.data.repository.Repository
import com.example.radioplayer.viewmodel.RadioPlayerViewModel
import com.example.radioplayer.viewmodel.FavoriteMusicViewModel
import com.example.radioplayer.viewmodel.RadioViewModel
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {
    @Provides
    @ScreenScope
    fun radioViewModel(repository: Repository): RadioViewModel = RadioViewModel(repository)

    @Provides
    @ScreenScope
    fun radioPlayerViewModel(repository: Repository
    ): RadioPlayerViewModel = RadioPlayerViewModel(repository)

    @Provides
    @ScreenScope
    fun favoriteMusicViewModel(repository: Repository
    ): FavoriteMusicViewModel = FavoriteMusicViewModel(repository)
}