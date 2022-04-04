package com.lab.esh1n.weather.weather.worker

import android.content.Context
import androidx.work.Data
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.esh1n.core_android.common.Failure
import com.esh1n.core_android.common.Loading
import com.esh1n.core_android.common.Success
import com.esh1n.core_android.common.catchError
import com.lab.esh1n.weather.WeatherApp
import com.lab.esh1n.weather.domain.IUILocalisator
import com.lab.esh1n.weather.domain.weather.WeatherRepository
import com.lab.esh1n.weather.utils.NotificationUtil
import com.lab.esh1n.weather.utils.WORKER_ERROR_DESCRIPTION
import io.reactivex.Single
import javax.inject.Inject

class SyncCurrentsWeatherWorker(context: Context, params: WorkerParameters) :
    RxWorker(context, params) {

    @Inject
    lateinit var weatherRepository: WeatherRepository


    @Inject
    lateinit var uiLocalizer: IUILocalisator

    override fun createWork(): Single<Result> {
        WeatherApp.getWorkerComponent(applicationContext).inject(this@SyncCurrentsWeatherWorker)
        return weatherRepository.fetchAndSaveAllPlacesCurrentWeathers()
            .andThen(weatherRepository.getCurrentWeatherSingle())
            .doOnSuccess {
                NotificationUtil.sendCurrentWeatherNotification(it, applicationContext, uiLocalizer)
            }
            .doOnError {
                NotificationUtil.sendFailureWeatherNotification(applicationContext, it.message)
            }
            .catchError()
            .map { result ->
                when (result) {
                    is Failure -> {
                        val failureResult = Data.Builder()
                            .putString(WORKER_ERROR_DESCRIPTION, result.error.message)
                            .build()
                        Result.failure(failureResult)
                    }
                    is Loading -> Result.success()
                    is Success -> Result.success()
                }
            }

    }
}

