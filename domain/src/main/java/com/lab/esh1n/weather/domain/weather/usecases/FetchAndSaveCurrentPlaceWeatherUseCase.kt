package com.lab.esh1n.weather.domain.weather.usecases

import com.lab.esh1n.weather.domain.UseCase
import com.lab.esh1n.weather.domain.base.ErrorsHandler
import com.lab.esh1n.weather.domain.base.Resource
import com.lab.esh1n.weather.domain.weather.WeatherRepository

class FetchAndSaveCurrentPlaceWeatherUseCase(private val weatherRepository: WeatherRepository, private val errorsHandler: ErrorsHandler)
    : UseCase<Any, Resource<Unit>> {

    override suspend fun execute(argument: Any): Resource<Unit> {
        return try {
            weatherRepository.fetchAndSaveCurrentWeather()
            Resource.success()
        } catch (ex: Throwable) {
            Resource.error(errorsHandler.handle(ex))
        }
    }

}