package com.lab.esh1n.weather.weather.worker

import android.content.Context
import androidx.work.Data
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.crashlytics.android.Crashlytics
import com.esh1n.core_android.ui.viewmodel.Resource
import com.lab.esh1n.weather.WeatherApp
import com.lab.esh1n.weather.domain.weather.places.usecase.DailyForecastSyncUseCase
import com.lab.esh1n.weather.utils.WORKER_ERROR_DESCRIPTION
import io.reactivex.Single
import javax.inject.Inject


class SyncAllPlacesForecastWorker(context: Context, params: WorkerParameters) :
        RxWorker(context, params) {

    @Inject
    lateinit var dailyForecastSyncUseCase: DailyForecastSyncUseCase

    override fun createWork(): Single<Result> {
        WeatherApp.getWorkerComponent(applicationContext).inject(this@SyncAllPlacesForecastWorker)
        return dailyForecastSyncUseCase
                .perform(Unit)
                .map { resource ->
                    val message = resource.errorModel?.message
                    Crashlytics.logException(Exception(message))
                    if (resource.status == Resource.Status.ERROR) {
                        val failureResult = Data.Builder()
                                .putString(WORKER_ERROR_DESCRIPTION, message)
                                .build()
                        Result.failure(failureResult)
                    } else {

                        Result.success()
                    }
                }

    }
}