package com.lab.esh1n.weather.weather.worker

import android.content.Context
import androidx.work.Data
import androidx.work.RxWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.crashlytics.android.Crashlytics
import com.esh1n.core_android.ui.viewmodel.Resource
import com.lab.esh1n.weather.WeatherApp
import com.lab.esh1n.weather.domain.weather.places.usecase.PrePopulatePlacesUseCase
import com.lab.esh1n.weather.utils.WORKER_ERROR_DESCRIPTION
import com.lab.esh1n.weather.utils.startAllPlacesForecastPeriodicSync
import com.lab.esh1n.weather.utils.startCurrentPlacePeriodicSync
import io.reactivex.Single
import javax.inject.Inject


class PrePopulatePlacesWorker(context: Context, params: WorkerParameters) :
        RxWorker(context, params) {

    @Inject
    lateinit var prepopulateUseCase: PrePopulatePlacesUseCase

    @Inject
    lateinit var workManager: WorkManager

    override fun createWork(): Single<Result> {
        WeatherApp.getWorkerComponent(applicationContext).inject(this@PrePopulatePlacesWorker)
        return prepopulateUseCase
                .perform(Unit)
                .doOnSuccess {
                    Crashlytics.log("PrePopulatePlacesWorker start sync workers")
                    with(workManager) {
                        startCurrentPlacePeriodicSync()
                        startAllPlacesForecastPeriodicSync()
                    }
                }
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