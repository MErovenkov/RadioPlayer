package com.example.radioplayer

import android.app.Application
import com.example.radioplayer.di.application.ApplicationComponent
import com.example.radioplayer.di.application.DaggerApplicationComponent
import com.example.radioplayer.util.CheckStatusNetwork

class RadioApplication: Application() {
    val applicationComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        CheckStatusNetwork.registerNetworkCallback(applicationContext)
    }
}