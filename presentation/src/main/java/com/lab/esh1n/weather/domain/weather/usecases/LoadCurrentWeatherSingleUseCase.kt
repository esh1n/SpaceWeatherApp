package com.lab.esh1n.weather.domain.weather.usecases

import com.lab.esh1n.weather.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.domain.weather.WeatherRepository
import io.reactivex.Single


class LoadCurrentWeatherSingleUseCase(private val weatherRepository: WeatherRepository) {

    fun perform(): Single<WeatherWithPlace> = weatherRepository.getCurrentWeatherSingle()
}