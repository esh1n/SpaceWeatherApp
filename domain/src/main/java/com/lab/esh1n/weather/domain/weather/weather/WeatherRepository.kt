package com.lab.esh1n.weather.domain.weather.weather

import android.content.SharedPreferences
import com.esh1n.utils_android.DateBuilder
import com.lab.esh1n.data.api.APIService
import com.lab.esh1n.data.cache.WeatherDB
import com.lab.esh1n.data.cache.dao.PlaceDAO
import com.lab.esh1n.data.cache.dao.WeatherDAO
import com.lab.esh1n.data.cache.entity.WeatherEntry
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.domain.BuildConfig
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
        const val UNITS = "metric"
    }

    fun fetchAndSaveWeather(id: Int): Completable {
        return api.getWeatherAsync(BuildConfig.APP_ID, id, UNITS).map {
            WeatherResponseMapper(id).map(it)
        }.flatMapCompletable { weatherEntry ->
            weatherDAO.saveWeather(weatherEntry)
        }
    }


    fun fetchAndSaveForecast(id: Int): Completable {
        return api.getWeatherAsync(BuildConfig.APP_ID, id, UNITS)
                .map { WeatherResponseMapper(id).map(it) }
                .zipWith(fetchForecastIfNeeded(id),
                        BiFunction<WeatherEntry, List<WeatherEntry>, List<WeatherEntry>> { currentWeatherEntry, forecastWeathers ->
                            val allWeathers = mutableListOf(currentWeatherEntry)
                            allWeathers.addAll(forecastWeathers)
                            allWeathers
                        }).flatMapCompletable { allWeathers ->
                    weatherDAO.saveWeathers(allWeathers)
                }
    }

    private fun fetchForecastIfNeeded(id: Int): Single<List<WeatherEntry>> {
        val fourDaysAfterNow = DateBuilder(Date()).plusDays(4).build()
        return weatherDAO
                .checkIf4daysForecastExist(id, fourDaysAfterNow)
                .map { count ->
                    count != 0
                }
                .flatMap { exist ->
                    if (exist) {
                        Single.just(listOf())
                    } else {
                        api.getForecastAsync(BuildConfig.APP_ID, id, UNITS)
                                .map { forecast ->
                                    ForecastWeatherMapper(id).map(forecast.list)
                                }
                    }
                }
    }


    fun getCurrentWeatherWithForecast(): Observable<List<WeatherWithPlace>> {
        val minus30Minutes = DateBuilder(Date()).minusMinutes(30).build()
        val plus5Days = DateBuilder(Date()).plusDays(5).build()
        return weatherDAO.getDetailedCurrentWeather(minus30Minutes, plus5Days).toObservable()
    }

    fun getCurrentWeatherSingle(): Single<WeatherWithPlace> {
        val now = Date()
        return weatherDAO.getCurrentWeather(now).firstOrError()
    }

    fun fetchAndSaveCurrentWeather(): Completable {
        return placeDAO.getCurrentCityId()
                .flatMapCompletable { fetchAndSaveWeather(it) }
    }

    fun fetchAndSaveAllPlacesCurrentWeathers(): Completable {
        return placeDAO.getAllPlacesIds()
                .flattenAsObservable { it }
                .flatMapCompletable { id -> fetchAndSaveWeather(id) }
    }
}