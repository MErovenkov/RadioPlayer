package com.example.radioplayer

import android.app.Application
import androidx.work.*
import com.example.radioplayer.di.application.ApplicationComponent
import com.example.radioplayer.di.application.DaggerApplicationComponent
import com.example.radioplayer.util.CheckStatusNetwork
import com.example.radioplayer.worker.DeleteWorker
import java.util.concurrent.TimeUnit

class RadioApplication: Application() {
    val applicationComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        initWorkers()
        CheckStatusNetwork.registerNetworkCallback(applicationContext)
    }

    private fun initWorkers() {
        val constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val deleteWorkerRequest =  PeriodicWorkRequest
            .Builder(DeleteWorker::class.java, 7, TimeUnit.DAYS)
            .addTag(DeleteWorker.NAME_WORKER)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).apply {
            enqueueUniquePeriodicWork(DeleteWorker.NAME_WORKER,
                ExistingPeriodicWorkPolicy.KEEP, deleteWorkerRequest)
        }
    }
}