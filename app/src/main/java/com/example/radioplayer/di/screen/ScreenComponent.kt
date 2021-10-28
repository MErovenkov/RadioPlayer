package com.example.radioplayer.di.screen

import com.example.radioplayer.viewmodel.DetailRadioViewModel
import com.example.radioplayer.viewmodel.RadioViewModel
import dagger.Subcomponent

@ScreenScope
@Subcomponent
interface ScreenComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): ScreenComponent
    }

    fun getRadioViewModel(): RadioViewModel
    fun getDetailRadioViewModel(): DetailRadioViewModel
}