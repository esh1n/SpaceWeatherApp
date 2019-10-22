package com.lab.esh1n.weather.domain.weather.weather

import com.esh1n.utils_android.DateBuilder
import com.lab.esh1n.data.api.APIService
import com.lab.esh1n.data.api.response.ForecastResponse
import com.lab.esh1n.data.api.response.WeatherResponse
import com.lab.esh1n.data.cache.AppPrefs
import com.lab.esh1n.data.cache.WeatherDB
import com.lab.esh1n.data.cache.dao.PlaceDAO
import com.lab.esh1n.data.cache.dao.WeatherDAO
import com.lab.esh1n.data.cache.entity.WeatherEntry
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.domain.BuildConfig
import com.lab.esh1n.weather.domain.weather.weather.mapper.EpochDateMapper
import com.lab.esh1n.weather.domain.weather.weather.mapper.ForecastWeatherMapper
import com.lab.esh1n.weather.domain.weather.weather.mapper.WeatherResponseMapper
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import java.util.*


class WeatherRepository constructor(private val api: APIService, database: WeatherDB, private val appPrefs: AppPrefs) {
    private val weatherDAO: WeatherDAO = database.weatherDAO()
    private val placeDAO: PlaceDAO = database.placeDAO()

    private fun fetchAndSaveWeather(id: Int): Completable {
        return fetchWeatherAsync(id)
                .map { WeatherResponseMapper(id).map(it) }
                .flatMapCompletable { weatherEntry -> weatherDAO.saveWeatherCompletable(weatherEntry) }
    }

    fun fetchAndSaveForecast(id: Int): Completable {
        return fetchWeatherAsync(id)
                .map { WeatherResponseMapper(id).map(it) }
                .zipWith(fetchForecastIfNeeded(id),
                        BiFunction<WeatherEntry, List<WeatherEntry>, List<WeatherEntry>> { currentWeatherEntry, forecastWeathers ->
                            val allWeathers = mutableListOf(currentWeatherEntry)
                            allWeathers.addAll(forecastWeathers)
                            allWeathers
                        }).flatMapCompletable { allWeathers ->
                    weatherDAO.saveWeathersCompletable(allWeathers)
                }
    }

    private fun fetchWeatherAsync(id: Int): Single<WeatherResponse> {
        return appPrefs.getLangAndUnitsSingle()
                .flatMap {
                    api.getWeatherAsync(BuildConfig.APP_ID, id, it.first, it.second)
                }
    }

    private fun fetchForecastAsync(id: Int): Single<ForecastResponse> {
        return appPrefs.getLangAndUnitsSingle()
                .flatMap {
                    api.getForecastAsync(BuildConfig.APP_ID, id, it.first, it.second)
                }
    }

    private fun fetchForecastIfNeeded(id: Int): Single<List<WeatherEntry>> {
        val fourDaysAfterNow = DateBuilder(Date()).plusDays(4).build()
        return weatherDAO
                .checkIf4daysForecastExist(id, fourDaysAfterNow)
                .map { count -> count != 0 }
                .flatMap { exist ->
                    if (exist) {
                        Single.just(listOf())
                    } else {
                        fetchForecastAsync(id)
                                .map { forecast ->
                                    val dateMapper = EpochDateMapper()
                                    ForecastWeatherMapper(id, dateMapper).map(forecast.list)
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

    fun fetchAndSaveAllPlacesCurrentWeather2(): Completable {
        return placeDAO.getAllPlacesIds()
                .flattenAsObservable { it }
                .flatMapCompletable { id ->
                    fetchAndSaveWeather(id)
                }
    }

    fun fetchAndSaveAllPlacesCurrentWeathers(): Completable {
        return placeDAO.getAllPlacesIds()
                .flatMap { ids ->
                    // val testIds= arrayListOf(472045)
                    val requests = ids.map { zipCurrentWeatherWithPlaceId(it) }
                    return@flatMap Single.zip<Pair<Int, WeatherResponse>, List<WeatherEntry>>(
                            requests,
                            Function {
                                return@Function mapResponsesToWeatherEntities(it)
                            }
                    )

                }.flatMapCompletable { weatherEntries ->
                    Completable.fromAction {
                        weatherDAO.updateCurrentWeathers(weatherEntries)
                    }
                }
    }

    private fun mapResponsesToWeatherEntities(responses: Array<Any>): List<WeatherEntry> {
        return responses.map {
            val idWithReponse = it as Pair<Int, WeatherResponse>
            WeatherResponseMapper(idWithReponse.first).map(idWithReponse.second)
        }
    }

    //
    private fun zipCurrentWeatherWithPlaceId(id: Int): Single<Pair<Int, WeatherResponse>> {
        return Single.zip(Single.just(id), fetchWeatherAsync(id), BiFunction<Int, WeatherResponse, Pair<Int, WeatherResponse>>
        { placeId, forecast -> Pair(placeId, forecast) })
    }
}