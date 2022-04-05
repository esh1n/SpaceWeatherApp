package com.lab.esh1n.weather.domain.places.usecase

import com.esh1n.core_android.error.ErrorsHandler
import com.esh1n.core_android.ui.viewmodel.Resource
import com.lab.esh1n.weather.domain.UseCase
import com.lab.esh1n.weather.domain.places.PlacesRepository
import com.lab.esh1n.weather.domain.weather.WeatherRepository
import io.reactivex.Single

class UpdateCurrentPlaceUseCase(private val placesRepository: PlacesRepository, private val weatherRepository: WeatherRepository, errorHandler: ErrorsHandler) : UseCase<Single<Resource<Unit>>, Int>(errorHandler) {
    override fun perform(args: Int): Single<Resource<Unit>> {
        return placesRepository
            .updateCurrentPlace(args)
            .andThen(weatherRepository.fetchAndSaveAllWeather(args))
                .andThen(Single.just(Resource.success()))
                .onErrorReturn { error ->
                    Resource.error(errorsHandler.handle(error))
                }
    }
}