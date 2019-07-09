package com.lab.esh1n.weather.domain.weather

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.lab.esh1n.data.api.APIService
import com.lab.esh1n.data.cache.WeatherDB
import com.lab.esh1n.data.cache.dao.WeatherDAO
import com.lab.esh1n.data.cache.entity.WeatherEntity
import com.lab.esh1n.weather.domain.weather.mapper.WeatherResponseMapper


class WeatherRepository constructor(private val api: APIService, database: WeatherDB, private val preferences: SharedPreferences) {
    private val weatherDAO: WeatherDAO = database.weatherDAO()

    companion object {
        const val APP_ID = "542ffd081e67f4512b705f89d2a611b2"
        const val UNITS = "metric"
        const val CITY_NAME = "CITY_NAME"
        const val DEFAULT_CITY = "Voronezh"
    }

    suspend fun saveCurrentCity(cityName: String) {
        preferences.edit().putString(CITY_NAME, cityName).apply()
    }

    suspend fun fetchAndSaveWeather(cityName: String) {
        val weatherResponse = api.getWeatherAsync(APP_ID, cityName, UNITS).await()
        Log.d("Weather", "id ${weatherResponse.id}")
        val weatherEntry = WeatherResponseMapper().map(weatherResponse)
        weatherDAO.saveWeather(weatherEntry)
    }

    suspend fun getWeatherByCity(cityName: String): WeatherEntity {
        return weatherDAO.getWeather(cityName)
    }

    fun getLiveCurrentWeather(): LiveData<WeatherEntity> {
        val currentCity = preferences.getString(CITY_NAME, DEFAULT_CITY) ?: DEFAULT_CITY
        return weatherDAO.getLiveWeather(currentCity)
    }

    suspend fun fetchAndSaveCurrentWeather() {
        val currentCity = preferences.getString(CITY_NAME, DEFAULT_CITY) ?: DEFAULT_CITY
        fetchAndSaveWeather(currentCity)
    }

    suspend fun getWeatherByCurrentCity(): WeatherEntity {
        val currentCity = preferences.getString(CITY_NAME, DEFAULT_CITY) ?: DEFAULT_CITY
        return weatherDAO.getWeather(currentCity)
    }
}

