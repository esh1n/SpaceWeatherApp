package com.lab.esh1n.weather.domain.weather.weather

import com.esh1n.utils_android.DateBuilder
import com.lab.esh1n.data.api.APIService
import com.lab.esh1n.data.api.response.CityResponse
import com.lab.esh1n.data.api.response.WeatherResponse
import com.lab.esh1n.data.cache.AppPrefs
import com.lab.esh1n.data.cache.Units
import com.lab.esh1n.data.cache.WeatherDB
import com.lab.esh1n.data.cache.dao.PlaceDAO
import com.lab.esh1n.data.cache.dao.WeatherDAO
import com.lab.esh1n.data.cache.entity.SunsetSunriseTimezonePlaceEntry
import com.lab.esh1n.data.cache.entity.WeatherEntry
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.domain.BuildConfig
import com.lab.esh1n.weather.domain.weather.weather.mapper.ForecastWeatherListMapper
import com.lab.esh1n.weather.domain.weather.weather.mapper.PlaceListMapper
import com.lab.esh1n.weather.domain.weather.weather.mapper.WeatherResponseListMapper
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import java.util.*


class WeatherRepository constructor(private val api: APIService, database: WeatherDB, private val appPrefs: AppPrefs) {
    private val weatherDAO: WeatherDAO = database.weatherDAO()
    private val placeDAO: PlaceDAO = database.placeDAO()

    fun fetchAndSaveForecast(id: Int): Completable {
        val serverUnits = appPrefs.getServerAPIUnits()
        return fetchWeatherAsync(id, serverUnits)
                .map { WeatherResponseListMapper(id, serverUnits).map(it) }
                .zipWith(fetchForecastIfNeeded(id),
                        BiFunction<WeatherEntry, Pair<SunsetSunriseTimezonePlaceEntry, List<WeatherEntry>>, Pair<SunsetSunriseTimezonePlaceEntry, List<WeatherEntry>>> { currentWeatherEntry, sunsetAndForecastWeathers ->
                            val allWeathers = mutableListOf(currentWeatherEntry)
                            allWeathers.addAll(sunsetAndForecastWeathers.second)
                            Pair(sunsetAndForecastWeathers.first, allWeathers)
                        }).flatMapCompletable { placeAndWeathers ->
                    val updatePlaceEntry = placeAndWeathers.first
                    placeDAO.updateSunrisesAndSunset(listOf(updatePlaceEntry))
                    weatherDAO.saveWeathersCompletable(placeAndWeathers.second)
                }
    }

    private fun fetchWeatherAsync(id: Int, serverUnits: Units): Single<WeatherResponse> {
        return appPrefs.getLanguageSingle()
                .flatMap { language ->
                    api.getWeatherAsync(BuildConfig.APP_ID, id, language, serverUnits.serverValue)
                }
    }


    private fun fetchForecastIfNeeded(placeId: Int): Single<Pair<SunsetSunriseTimezonePlaceEntry, List<WeatherEntry>>> {
        val fourDaysAfterNow = DateBuilder(Date()).plusDays(4).build()
        return weatherDAO
                .checkIf4daysForecastExist(placeId, fourDaysAfterNow)
                .map { count -> count != 0 }
                .flatMap { exist ->
                    if (exist) {
                        Single.just(Pair(SunsetSunriseTimezonePlaceEntry.createEmpty(), listOf()))
                    } else {
                        val serverUnits = appPrefs.getServerAPIUnits()
                        appPrefs
                                .getLanguageSingle()
                                .flatMap { language ->
                                    api.getForecastAsync(BuildConfig.APP_ID, placeId, lang = language, units = serverUnits.serverValue)
                                }
                                .map { forecast ->
                                    val updatePlaceModel = PlaceListMapper().map(forecast.city!!)
                                    val weathers = ForecastWeatherListMapper(placeId, serverUnits).map(forecast.list)
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

    fun getCurrentPlaceSunsetAndSunrise() = placeDAO.getCurrentSunsetSunriseInfo().toObservable()

    fun getCurrentWeatherSingle(): Single<WeatherWithPlace> {
        val now = DateBuilder(Date()).build()
        return weatherDAO.getCurrentWeather(now).firstOrError()
    }

    fun fetchAndSaveAllPlacesCurrentWeathers(): Completable {
        val serverUnits = appPrefs.getServerAPIUnits()
        return placeDAO.getPlaceIdsToSync()
                .flatMap { ids ->
                    val requests = ids.map { zipCurrentWeatherWithPlaceId(it, serverUnits) }
                    return@flatMap Single.zip<Pair<Int, WeatherResponse>, Pair<List<SunsetSunriseTimezonePlaceEntry>, List<WeatherEntry>>>(
                            requests,
                            Function {
                                return@Function mapResponsesToWeatherEntities(it, serverUnits)
                            }
                    )

                }.flatMapCompletable { sunsetsAndWeatherEntries ->
                    Completable.fromAction {
                        placeDAO.updateSunrisesAndSunset(sunsetsAndWeatherEntries.first)
                        weatherDAO.updateCurrentWeathers(sunsetsAndWeatherEntries.second)
                    }
                }
    }

    @SuppressWarnings
    private fun mapResponsesToWeatherEntities(responses: Array<Any>, serverUnits: Units): Pair<List<SunsetSunriseTimezonePlaceEntry>, List<WeatherEntry>> {
        val weathers = responses.map {
            val idWithResponse = it as Pair<Int, WeatherResponse>
            WeatherResponseListMapper(idWithResponse.first, serverUnits).map(idWithResponse.second)
        }
        val sunsets = responses.map {
            val idWithResponse = it as Pair<Int, WeatherResponse>
            val weather = idWithResponse.second
            PlaceListMapper().map(CityResponse(id = idWithResponse.first,
                sunrise = weather.sys?.sunrise ?: 0L,
                sunset = weather.sys?.sunset ?: 0L,
                timezone = weather.timezone))
        }.distinctBy { it.id }
        return Pair(sunsets, weathers)
    }

    //
    private fun zipCurrentWeatherWithPlaceId(id: Int, serverUnits: Units): Single<Pair<Int, WeatherResponse>> {
        return Single.zip(Single.just(id), fetchWeatherAsync(id, serverUnits), BiFunction<Int, WeatherResponse, Pair<Int, WeatherResponse>>
        { placeId, forecast -> Pair(placeId, forecast) })
    }

    fun getAvailableDaysForPlace(placeId: Int): Flowable<Triple<Int, String, List<Date>>> {
        val almostNow = DateBuilder(Date()).build()
        return weatherDAO
                .getAllWeathersForCity(placeId, almostNow)
                .filter { weathers -> !weathers.isNullOrEmpty() }
                .map { weathers ->
                    val timeZone = if (weathers.isNullOrEmpty()) "Europe/Moscow" else weathers[0].timezone
                    val dates = weathers.distinctBy {
                        val day = DateBuilder(it.epochDateMills, timeZone).getDayOfYear()
                        day
                    }.map { it.epochDateMills }
                    return@map Triple(placeId, timeZone, dates)
                }
    }

    fun getWeathersForPlaceAtDay(placeId: Int, date: Date, timeZone: String): Single<List<WeatherWithPlace>> {
        val dayStart = DateBuilder(date, timeZone).endOfNight().build()
        val dayEnd = DateBuilder(date, timeZone).nextDay().endOfNight().build()
        return weatherDAO.getDayWeathersForCity(placeId, dayStart, dayEnd)
    }
}