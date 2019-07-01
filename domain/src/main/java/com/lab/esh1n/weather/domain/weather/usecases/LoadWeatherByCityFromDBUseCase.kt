package com.lab.esh1n.weather.domain.weather.usecases

import com.lab.esh1n.data.cache.entity.WeatherEntity
import com.lab.esh1n.weather.domain.UseCase
import com.lab.esh1n.weather.domain.base.ErrorsHandler
import com.lab.esh1n.weather.domain.base.Resource
import com.lab.esh1n.weather.domain.weather.WeatherRepository

class LoadWeatherByCityFromDBUseCase(private val weatherRepository: WeatherRepository, private val errorsHandler: ErrorsHandler)
    : UseCase<String, Resource<WeatherEntity>> {
    override suspend fun execute(argument: String): Resource<WeatherEntity> {

        return try {
            val weatherEntity = weatherRepository.getWeatherByCity(argument)
            return Resource.success(weatherEntity)
        } catch (ex: Throwable) {
            Resource.error(errorsHandler.handle(ex))
        }

    }
}
