package com.lab.esh1n.weather.domain.weather.weather

import android.content.SharedPreferences
import com.lab.esh1n.data.api.APIService
import com.lab.esh1n.data.cache.WeatherDB
import com.lab.esh1n.data.cache.dao.PlaceDAO
import com.lab.esh1n.data.cache.dao.WeatherDAO
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.domain.weather.weather.mapper.WeatherResponseMapper
import io.reactivex.Completable
import io.reactivex.Observable


class WeatherRepository constructor(private val api: APIService, database: WeatherDB, private val preferences: SharedPreferences) {
    private val weatherDAO: WeatherDAO = database.weatherDAO()
    private val placeDAO: PlaceDAO = database.placeDAO()

    companion object {
        const val APP_ID = "542ffd081e67f4512b705f89d2a611b2"
        const val UNITS = "metric"
    }

    fun fetchAndSaveWeather(id: Int): Completable {
        return api.getWeatherAsync(APP_ID, id, UNITS).map {
            WeatherResponseMapper(id).map(it)
        }.flatMapCompletable { weatherEntry ->
            weatherDAO.saveWeather(weatherEntry)
        }
    }

    fun getCurrentWeather(): Observable<WeatherWithPlace> {
        return weatherDAO.getCurrentWeather().toObservable()
    }

    fun fetchAndSaveCurrentWeather(): Completable {
        return placeDAO.getCurrentCityId()
                .flatMapCompletable { fetchAndSaveWeather(it) }
    }
}