package com.example.radioplayer.di.screen

import com.example.radioplayer.viewmodel.RadioPlayerViewModel
import com.example.radioplayer.viewmodel.FavoriteMusicViewModel
import com.example.radioplayer.viewmodel.RadioViewModel
import dagger.Subcomponent

@ScreenScope
@Subcomponent(modules = [ViewModelModule::class])
interface ScreenComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): ScreenComponent
    }

    fun getRadioViewModel(): RadioViewModel
    fun getRadioPlayerViewModel(): RadioPlayerViewModel
    fun getFavoriteMusicViewModel(): FavoriteMusicViewModel
}