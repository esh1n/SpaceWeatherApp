package com.lab.esh1n.weather.domain.weather.weather

import android.content.SharedPreferences
import com.lab.esh1n.data.api.APIService
import com.lab.esh1n.data.api.response.CityResponse
import com.lab.esh1n.data.api.response.ForecastResponse
import com.lab.esh1n.data.api.response.WeatherResponse
import com.lab.esh1n.data.cache.WeatherDB
import com.lab.esh1n.data.cache.dao.PlaceDAO
import com.lab.esh1n.data.cache.dao.WeatherDAO
import com.lab.esh1n.data.cache.entity.WeatherEntry
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.domain.weather.weather.mapper.ForecastWeatherMapper
import com.lab.esh1n.weather.domain.weather.weather.mapper.WeatherResponseMapper
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.*


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

    fun fetchAndSaveForecast(id: Int): Completable {
        return Single.zip<WeatherResponse, ForecastResponse, Pair<CityResponse?, List<WeatherEntry>>>(api.getWeatherAsync(APP_ID, id, UNITS),
                api.getForecastAsync(APP_ID, id, UNITS), BiFunction { currentWeatherResponse, forecast ->
            val city = forecast.city
            val currentWeather = WeatherResponseMapper(id).map(currentWeatherResponse)
            val forecastWeathers = ForecastWeatherMapper(id)
                    .map(forecast.list)
            val allWeathers = mutableListOf(currentWeather)
            allWeathers.addAll(forecastWeathers)
            Pair(city, allWeathers)

        }).flatMapCompletable { pair ->
            weatherDAO.saveWeathers(pair.second)
        }

    }

    fun getCurrentWeather(): Observable<WeatherWithPlace> {
        val now = Date()
        return weatherDAO.getCurrentWeather(now).toObservable()
    }

    fun getCurrentWeatherSingle(): Single<WeatherWithPlace> {
        return getCurrentWeather().firstOrError()
    }

    fun fetchAndSaveCurrentWeather(): Completable {
        return placeDAO.getCurrentCityId()
                .flatMapCompletable { fetchAndSaveWeather(it) }
    }
}