package com.lab.esh1n.weather.domain.weather.weather.usecases

import com.esh1n.core_android.error.ErrorsHandler
import com.esh1n.core_android.ui.viewmodel.Resource
import com.lab.esh1n.weather.domain.weather.UseCase
import com.lab.esh1n.weather.domain.weather.weather.WeatherRepository
import io.reactivex.Single
import java.util.*

class LoadPlaceAvailableForecastDaysUseCase(private val weatherRepository: WeatherRepository,
                                            errorsHandler: ErrorsHandler) :UseCase<Single<Resource<List<Date>>>,Int>(errorsHandler) {
    override fun perform(args: Int): Single<Resource<List<Date>>> {
        return weatherRepository.getAvailableDaysForPlace()
                .map { days -> Resource.success(days) }
                .onErrorReturn { error ->
                    Resource.error(errorsHandler.handle(error))
                }
    }
}