package com.lab.esh1n.weather.weather.worker

import android.content.Context
import androidx.work.Data
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.esh1n.core_android.ui.viewmodel.Resource
import com.lab.esh1n.weather.WeatherApp
import com.lab.esh1n.weather.domain.weather.places.usecase.FetchAndSaveAllPlacesForecastUseCase
import com.lab.esh1n.weather.utils.WORKER_ERROR_DESCRIPTION
import io.reactivex.Single
import javax.inject.Inject


class SyncAllPlacesForecastWorker(context: Context, params: WorkerParameters) :
        RxWorker(context, params) {

    @Inject
    lateinit var fetchAndSaveAllPlacesForecastUseCase: FetchAndSaveAllPlacesForecastUseCase

    override fun createWork(): Single<Result> {
        WeatherApp.getWorkerComponent(applicationContext).inject(this@SyncAllPlacesForecastWorker)
        return fetchAndSaveAllPlacesForecastUseCase
                .perform(Unit)
                .map { resource ->
                    if (resource.status == Resource.Status.ERROR) {
                        val failureResult = Data.Builder()
                                .putString(WORKER_ERROR_DESCRIPTION, resource.errorModel!!.message)
                                .build()
                        Result.failure(failureResult)
                    } else {

                        Result.success()
                    }
                }

    }
}