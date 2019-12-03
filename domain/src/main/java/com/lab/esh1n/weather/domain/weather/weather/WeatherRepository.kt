package com.lab.esh1n.weather.domain.weather.weather

import com.esh1n.utils_android.DateBuilder
import com.lab.esh1n.data.api.APIService
import com.lab.esh1n.data.api.response.CityResponse
import com.lab.esh1n.data.api.response.ForecastResponse
import com.lab.esh1n.data.api.response.WeatherResponse
import com.lab.esh1n.data.cache.AppPrefs
import com.lab.esh1n.data.cache.WeatherDB
import com.lab.esh1n.data.cache.dao.PlaceDAO
import com.lab.esh1n.data.cache.dao.WeatherDAO
import com.lab.esh1n.data.cache.entity.UpdatePlaceEntry
import com.lab.esh1n.data.cache.entity.WeatherEntry
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.domain.BuildConfig
import com.lab.esh1n.weather.domain.weather.weather.mapper.ForecastWeatherMapper
import com.lab.esh1n.weather.domain.weather.weather.mapper.PlaceMapper
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
                        BiFunction<WeatherEntry, Pair<UpdatePlaceEntry, List<WeatherEntry>>, Pair<UpdatePlaceEntry, List<WeatherEntry>>> { currentWeatherEntry, sunsetAndForecastWeathers ->
                            val allWeathers = mutableListOf(currentWeatherEntry)
                            allWeathers.addAll(sunsetAndForecastWeathers.second)
                            Pair(sunsetAndForecastWeathers.first, allWeathers)
                        }).flatMapCompletable { placeAndWeathers ->
                    val updatePlaceEntry = placeAndWeathers.first
                    placeDAO.updateSunsetSunrise(updatePlaceEntry.id, updatePlaceEntry.sunrise, updatePlaceEntry.sunset)
                    weatherDAO.saveWeathersCompletable(placeAndWeathers.second)
                }
    }

    private fun fetchWeatherAsync(id: Int): Single<WeatherResponse> {
        return appPrefs.getLangAndUnitsSingle()
                .flatMap {
                    api.getWeatherAsync(BuildConfig.APP_ID, id, it.first, it.second)
                }
    }

    private fun fetchForecastAsync(id: Int): Single<ForecastResponse> {
        return appPrefs
                .getLangAndUnitsSingle()
                .flatMap {
                    api.getForecastAsync(BuildConfig.APP_ID, id, it.first, it.second)
                }
    }

    private fun fetchForecastIfNeeded(id: Int): Single<Pair<UpdatePlaceEntry, List<WeatherEntry>>> {
        val fourDaysAfterNow = DateBuilder(Date()).plusDays(4).build()
        return weatherDAO
                .checkIf4daysForecastExist(id, fourDaysAfterNow)
                .map { count -> count != 0 }
                .flatMap { exist ->
                    if (exist) {
                        Single.just(Pair(UpdatePlaceEntry.createEmpty(), listOf()))
                    } else {
                        fetchForecastAsync(id)
                                .map { forecast ->

                                    val updatePlaceModel = PlaceMapper().map(forecast.city!!)
                                    val weathers = ForecastWeatherMapper(id).map(forecast.list)
                                    return@map Pair(updatePlaceModel, weathers)
                                }
                    }
                }
    }

    fun getCurrentWeatherWithForecast(): Observable<List<WeatherWithPlace>> {
        val minus30Minutes = DateBuilder(Date()).minusMinutes(30).build()
        val plus5Days = DateBuilder(Date()).plusDays(5).build()
        return weatherDAO.getDetailedCurrentWeather(minus30Minutes, plus5Days).toObservable()
    }

    fun getCurrentPlaceSunsetAndSunrise(): Observable<UpdatePlaceEntry> {
        return placeDAO.getCurrentSunsetSunriseInfo().toObservable();
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
                    return@flatMap Single.zip<Pair<Int, WeatherResponse>, Pair<List<UpdatePlaceEntry>, List<WeatherEntry>>>(
                            requests,
                            Function {
                                return@Function mapResponsesToWeatherEntities(it)
                            }
                    )

                }.flatMapCompletable { sunsetsAndWeatherEntries ->
                    Completable.fromAction {
                        placeDAO.updateSunrisesAndSunset(sunsetsAndWeatherEntries.first)
                        weatherDAO.updateCurrentWeathers(sunsetsAndWeatherEntries.second)
                    }
                }
    }

    private fun mapResponsesToWeatherEntities(responses: Array<Any>): Pair<List<UpdatePlaceEntry>, List<WeatherEntry>> {
        val weathers = responses.map {
            val idWithReponse = it as Pair<Int, WeatherResponse>
            WeatherResponseMapper(idWithReponse.first).map(idWithReponse.second)
        }
        val sunsets = responses.map {
            val idWithReponse = it as Pair<Int, WeatherResponse>
            PlaceMapper().map(CityResponse(id = idWithReponse.first,
                    sunrise = idWithReponse.second.sys?.sunrise ?: 0L,
                    sunset = idWithReponse.second.sys?.sunset ?: 0L))
        }.distinctBy { it.id }
        return Pair(sunsets, weathers)
    }

    //
    private fun zipCurrentWeatherWithPlaceId(id: Int): Single<Pair<Int, WeatherResponse>> {
        return Single.zip(Single.just(id), fetchWeatherAsync(id), BiFunction<Int, WeatherResponse, Pair<Int, WeatherResponse>>
        { placeId, forecast -> Pair(placeId, forecast) })
    }

    fun getAvailableDaysForPlace(placeId:Int): Single<List<Date>> {
        val almostNow = Date()
        return weatherDAO
                .getAllWeathersForCity(placeId,almostNow)
                .map { weathers->
                    return@map weathers.distinctBy { DateBuilder(it.epochDateMills).getDay() }.map { it.epochDateMills }
                }
    }
}