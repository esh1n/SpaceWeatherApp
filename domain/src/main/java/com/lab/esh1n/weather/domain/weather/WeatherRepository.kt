package com.lab.esh1n.weather.domain.weather

import androidx.lifecycle.LiveData
import com.lab.esh1n.data.api.APIService
import com.lab.esh1n.data.cache.WeatherDB
import com.lab.esh1n.data.cache.dao.WeatherDAO
import com.lab.esh1n.data.cache.entity.WeatherEntity
import com.lab.esh1n.weather.domain.weather.mapper.WeatherResponseMapper


class WeatherRepository constructor(private val api: APIService, database: WeatherDB) {
    private val weatherDAO: WeatherDAO = database.weatherDAO()

    companion object {
        const val APP_ID = "542ffd081e67f4512b705f89d2a611b2"
        const val UNITS = "metric"
    }

    suspend fun fetchAndSaveWeather(cityName: String) {
        val weatherResponse = api.getWeatherAsync(APP_ID, cityName, UNITS).await()
        val weatherEntry = WeatherResponseMapper().map(weatherResponse)
        weatherDAO.saveWeather(weatherEntry)
    }

    suspend fun getWeatherByCity(cityName: String): WeatherEntity {
        return weatherDAO.getWeather(cityName)
    }

    fun getLiveWeatherByCity(cityName: String): LiveData<WeatherEntity> {
        return weatherDAO.getLiveWeather(cityName)
    }
}

