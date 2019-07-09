package com.lab.esh1n.weather.domain.weather.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.lab.esh1n.data.cache.entity.WeatherEntity
import com.lab.esh1n.weather.domain.UseCase
import com.lab.esh1n.weather.domain.base.Resource
import com.lab.esh1n.weather.domain.weather.WeatherRepository

class LoadCurrentWeatherLiveDataUseCase(private val weatherRepository: WeatherRepository)
    : UseCase<Unit, LiveData<Resource<WeatherEntity>>> {
    override suspend fun execute(argument: Unit): LiveData<Resource<WeatherEntity>> {
        return Transformations.map(weatherRepository.getLiveCurrentWeather()) { Resource.success(it) }
    }
}