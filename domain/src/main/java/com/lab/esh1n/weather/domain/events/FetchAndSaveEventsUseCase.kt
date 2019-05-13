package com.lab.esh1n.weather.domain.events

import com.lab.esh1n.data.api.APIService
import com.lab.esh1n.data.cache.dao.WeatherDAO
import com.lab.esh1n.weather.domain.UseCase
import com.lab.esh1n.weather.domain.base.ErrorsHandler
import com.lab.esh1n.weather.domain.base.Resource
import com.lab.esh1n.weather.domain.events.mapper.WeatherResponseMapper

class FetchAndSaveEventsUseCase(private val api: APIService, private val weatherDAO: WeatherDAO, private val errorsHandler: ErrorsHandler)
    : UseCase<String, Resource<Unit>> {

    private val weatherResponseMapper = WeatherResponseMapper()

    override suspend fun execute(argument: String): Resource<Unit> {
        return try {
            val weatherResponse = api.getWeatherAsync(APP_ID, argument, UNITS).await()
            val weatherEntry = weatherResponseMapper.map(weatherResponse)
            weatherDAO.saveWeather(weatherEntry)
            Resource.success()
        } catch (ex: Throwable) {
            Resource.error(errorsHandler.handle(ex))
        }
    }

    companion object {
        const val APP_ID = "542ffd081e67f4512b705f89d2a611b2"
        const val UNITS = "metric"
    }
}