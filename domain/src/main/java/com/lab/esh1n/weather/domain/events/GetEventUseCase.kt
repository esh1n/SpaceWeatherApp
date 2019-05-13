package com.lab.esh1n.weather.domain.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.lab.esh1n.data.cache.dao.WeatherDAO
import com.lab.esh1n.data.cache.entity.WeatherEntity
import com.lab.esh1n.weather.domain.UseCase
import com.lab.esh1n.weather.domain.base.ErrorsHandler
import com.lab.esh1n.weather.domain.base.Resource

class GetEventUseCase(private val weatherDAO: WeatherDAO, private val errorsHandler: ErrorsHandler)
    : UseCase<String, LiveData<Resource<WeatherEntity>>> {
    override suspend fun execute(argument: String): LiveData<Resource<WeatherEntity>> {
        return Transformations.map(weatherDAO.getWeather(argument)) { Resource.success(it) }
    }
}
