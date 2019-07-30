package com.lab.esh1n.weather.domain.weather.weather

import android.content.SharedPreferences
import com.lab.esh1n.data.api.APIService
import com.lab.esh1n.data.api.response.WeatherResponse
import com.lab.esh1n.data.cache.WeatherDB
import com.lab.esh1n.data.cache.dao.PlaceDAO
import com.lab.esh1n.data.cache.dao.WeatherDAO
import com.lab.esh1n.data.cache.entity.WeatherEntry
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.domain.weather.weather.mapper.WeatherResponseMapper
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction


class WeatherRepository constructor(private val api: APIService, database: WeatherDB, private val preferences: SharedPreferences) {
    private val weatherDAO: WeatherDAO = database.weatherDAO()
    private val placeDAO: PlaceDAO = database.placeDAO()

    companion object {
        const val APP_ID = "542ffd081e67f4512b705f89d2a611b2"
        const val UNITS = "metric"
        const val CITY_NAME = "CITY_NAME"
        const val DEFAULT_CITY = "Voronezh"
    }

    fun saveCurrentCity(cityName: String): Completable {
        return Completable.fromAction {
            preferences.edit().putString(CITY_NAME, cityName).apply()
        }
    }

    fun fetchAndSaveWeather(cityName: String): Completable {
        return Single.zip<WeatherResponse, Int, WeatherEntry>(api.getWeatherAsync(APP_ID, cityName, UNITS),
                placeDAO.getPlaceIdByName(cityName), BiFunction { response, id ->
            WeatherResponseMapper(id).map(response)
        }).flatMapCompletable { weatherEntry ->
            weatherDAO.saveWeather(weatherEntry)
        }
    }

    fun getCurrentWeather(): Observable<WeatherWithPlace> {
        val currentCity = preferences.getString(CITY_NAME, DEFAULT_CITY) ?: DEFAULT_CITY
        return weatherDAO.getWeather(currentCity).toObservable()
    }

    fun fetchAndSaveCurrentWeather(): Completable {
        return Single
                .just(preferences.getString(CITY_NAME, DEFAULT_CITY) ?: DEFAULT_CITY)
                .flatMapCompletable { fetchAndSaveWeather(it) }
    }
}