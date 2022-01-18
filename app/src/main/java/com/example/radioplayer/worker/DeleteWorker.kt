package com.example.radioplayer.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.radioplayer.data.repository.Repository
import com.example.radioplayer.util.extension.getApplicationComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import javax.inject.Inject

/**
 * Removes radio station from database if they are not in .json
 * */
class DeleteWorker(context: Context, params: WorkerParameters): Worker(context, params) {

    companion object {
        const val NAME_WORKER = "deleteRadioDate"
    }

    @Inject
    lateinit var repository: Repository

    init {
        getApplicationComponent().inject(this)
    }

    override fun doWork(): Result {
        return try {
            deleteRadioDate()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private fun deleteRadioDate() = runBlocking {
        val radioTitleDatabase: ArrayList<String> = repository.getRadioDateList()
                                                         .first()
                                                         .map { it.title } as ArrayList<String>

        val radioTitleJson: Set<String> = repository.getRadioItemList()
                                                    .first()
                                                    .map { it.mediaMetadata.title.toString() }
                                                    .toSet()

        if (radioTitleDatabase.removeAll(radioTitleJson)) {
            radioTitleDatabase.forEach {
                repository.deleteRadioByTitle(it)
            }
        }
    }
}