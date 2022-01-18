package com.example.radioplayer.util.extension

import android.app.Service
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.Worker
import com.example.radioplayer.RadioApplication
import com.example.radioplayer.di.activity.ActivityComponent
import com.example.radioplayer.di.application.ApplicationComponent
import com.example.radioplayer.di.screen.ScreenComponent

fun Worker.getApplicationComponent(): ApplicationComponent {
    return (this.applicationContext as RadioApplication).applicationComponent
}

fun Service.getApplicationComponent(): ApplicationComponent {
    return (this.applicationContext as RadioApplication).applicationComponent
}

fun ComponentActivity.getActivityComponent(): ActivityComponent {
    return (this.applicationContext as RadioApplication).applicationComponent
        .activityComponent().create(this)
}

fun ActivityComponent.buildScreenComponent(): ScreenComponent {
    return this.screenComponent().build()
}

@Suppress("UNCHECKED_CAST")
@Composable
inline fun <reified T : ViewModel> daggerViewModel(
    key: String? = null,
    crossinline viewModelInstanceCreator: () -> T
): T =
    androidx.lifecycle.viewmodel.compose.viewModel(
        modelClass = T::class.java,
        key = key,
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return viewModelInstanceCreator() as T
            }
        }
    )