package com.example.radioplayer.util.extension

import androidx.activity.ComponentActivity
import com.example.radioplayer.RadioApplication
import com.example.radioplayer.di.activity.ActivityComponent
import com.example.radioplayer.di.screen.ScreenComponent

fun ComponentActivity.getActivityComponent(): ActivityComponent {
    return (this.applicationContext as RadioApplication).applicationComponent
        .activityComponent().create(this)
}

fun ActivityComponent.buildScreenComponent(): ScreenComponent {
    return this.screenComponent().build()
}