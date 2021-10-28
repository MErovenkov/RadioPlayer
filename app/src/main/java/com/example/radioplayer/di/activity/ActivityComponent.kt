package com.example.radioplayer.di.activity

import android.content.Context
import com.example.radioplayer.di.screen.ScreenComponent
import com.example.radioplayer.ui.MainActivity
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [SubScreenModule::class, ViewModelModule::class])
interface ActivityComponent {
    interface Holder {
        val activityComponent: ActivityComponent
    }

    @Subcomponent.Factory
    interface Factory {
        fun create(@ActivityContext @BindsInstance context: Context): ActivityComponent
    }

    fun screenComponent(): ScreenComponent.Builder

    fun inject(mainActivity: MainActivity)
}