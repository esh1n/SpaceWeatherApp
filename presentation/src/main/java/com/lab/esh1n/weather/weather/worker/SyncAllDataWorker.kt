package com.lab.esh1n.weather.weather.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.lab.esh1n.weather.WeatherApp
import com.lab.esh1n.weather.domain.base.Resource
import com.lab.esh1n.weather.domain.events.FetchAndSaveEventsUseCase
import com.lab.esh1n.weather.utils.WORKER_ERROR_DESCRIPTION
import javax.inject.Inject


class SyncAllDataWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    @Inject
    lateinit var syncDataWorker: FetchAndSaveEventsUseCase


    override fun doWork(): Result {
        WeatherApp.getWorkerComponent(applicationContext).inject(this)
        val syncResult = syncDataWorker.execute(Unit).blockingGet()
        return if (syncResult.status == Resource.Status.ERROR) {
            val failureResult = workDataOf(WORKER_ERROR_DESCRIPTION to syncResult.errorModel)
            Result.failure(failureResult)
        } else {
            Result.success()
        }
    }

}