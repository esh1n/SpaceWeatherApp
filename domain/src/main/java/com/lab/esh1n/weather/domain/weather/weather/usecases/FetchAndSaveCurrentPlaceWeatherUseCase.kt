package com.lab.esh1n.weather.domain.weather.weather.usecases

import com.esh1n.core_android.error.ErrorsHandler
import com.esh1n.core_android.ui.viewmodel.Resource
import com.lab.esh1n.weather.domain.weather.UseCase
import com.lab.esh1n.weather.domain.weather.weather.repository.WeatherRepository
import io.reactivex.Single


class FetchAndSaveCurrentPlaceWeatherUseCase(private val weatherRepository: WeatherRepository, errorsHandler: ErrorsHandler) :
        UseCase<Single<Resource<Unit>>, Any>(errorsHandler) {

    override fun perform(args: Any): Single<Resource<Unit>> {
        return weatherRepository
                .fetchAndSaveCurrentWeather()
                .andThen(Single.just(Resource.success()))
                .onErrorReturn { error ->
                    Resource.error(errorsHandler.handle(error))
                }
    }

}
