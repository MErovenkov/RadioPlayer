package com.example.radioplayer.di.application

import android.content.Context
import com.example.radioplayer.di.activity.ActivityComponent
import com.example.radioplayer.ui.exoplayer.service.RadioPlayerService
import com.example.radioplayer.worker.DeleteWorker
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [SubActivityModule::class, RepositoryModule::class])
interface ApplicationComponent {
    @Component.Factory
    interface Factory {
        fun create(@ApplicationContext @BindsInstance context: Context): ApplicationComponent
    }

    fun activityComponent(): ActivityComponent.Factory

    fun inject(radioPlayerService: RadioPlayerService)
    fun inject(deleteWorker: DeleteWorker)
}