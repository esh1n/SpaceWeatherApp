package com.lab.esh1n.weather.domain.weather.usecases

import com.lab.esh1n.data.cache.entity.WeatherEntity
import com.lab.esh1n.weather.domain.UseCase
import com.lab.esh1n.weather.domain.base.ErrorsHandler
import com.lab.esh1n.weather.domain.base.Resource
import com.lab.esh1n.weather.domain.weather.WeatherRepository

class LoadCurrentWeatherUseCase(private val weatherRepository: WeatherRepository, private val errorsHandler: ErrorsHandler)
    : UseCase<Unit, Resource<WeatherEntity>> {
    override suspend fun execute(argument: Unit): Resource<WeatherEntity> {

        return try {
            val weatherEntity = weatherRepository.getWeatherByCurrentCity()
            return Resource.success(weatherEntity)
        } catch (ex: Throwable) {
            Resource.error(errorsHandler.handle(ex))
        }

    }
}
