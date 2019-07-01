package com.lab.esh1n.weather.weather.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.lab.esh1n.weather.WeatherApp
import com.lab.esh1n.weather.domain.base.Resource
import com.lab.esh1n.weather.domain.weather.usecases.FetchAndSaveWeatherUseCase
import com.lab.esh1n.weather.utils.WORKER_ERROR_DESCRIPTION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class SyncAllDataWorker(context: Context, params: WorkerParameters) :
        CoroutineWorker(context, params) {

    @Inject
    lateinit var syncDataWorker: FetchAndSaveWeatherUseCase

    override val coroutineContext = Dispatchers.IO

    override suspend fun doWork(): Result = coroutineScope {
        WeatherApp.getWorkerComponent(applicationContext).inject(this@SyncAllDataWorker)
        val syncResult = syncDataWorker.execute("Voronezh")
        if (syncResult.status == Resource.Status.ERROR) {
            val failureResult = workDataOf(WORKER_ERROR_DESCRIPTION to syncResult.errorModel)
            Result.failure(failureResult)
        } else {
            Result.success()
        }
    }
}