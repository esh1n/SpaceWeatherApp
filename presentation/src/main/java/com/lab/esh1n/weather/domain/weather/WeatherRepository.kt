package com.lab.esh1n.weather.domain.weather

import com.esh1n.utils_android.DateBuilder
import com.lab.esh1n.weather.BuildConfig
import com.lab.esh1n.weather.data.api.APIService
import com.lab.esh1n.weather.data.api.response.CityResponse
import com.lab.esh1n.weather.data.api.response.WeatherResponse
import com.lab.esh1n.weather.data.cache.WeatherDB
import com.lab.esh1n.weather.data.cache.dao.PlaceDAO
import com.lab.esh1n.weather.data.cache.dao.WeatherDAO
import com.lab.esh1n.weather.data.cache.entity.SunsetSunriseTimezonePlaceEntry
import com.lab.esh1n.weather.data.cache.entity.WeatherEntry
import com.lab.esh1n.weather.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.domain.prefs.IPrefsInteractor
import com.lab.esh1n.weather.domain.prefs.Units
import com.lab.esh1n.weather.domain.weather.mapper.ForecastWeatherListMapper
import com.lab.esh1n.weather.domain.weather.mapper.PlaceListMapper
import com.lab.esh1n.weather.domain.weather.mapper.WeatherResponseListMapper
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*


class WeatherRepository constructor(
    private val api: APIService,
    database: WeatherDB,
    private val appPrefs: IPrefsInteractor
) {
    private val weatherDAO: WeatherDAO = database.weatherDAO()
    private val placeDAO: PlaceDAO = database.placeDAO()

    fun fetchAndSaveAllWeather(placeId: Int): Completable =
        fetchCompleteForecast(placeId).flatMapCompletable { it.save() }

    private fun fetchCompleteForecast(placeId: Int): Single<Pair<List<SunsetSunriseTimezonePlaceEntry>, List<WeatherEntry>>> {
        val serverUnits = appPrefs.getServerAPIUnits()
        return fetchWeatherAsync(placeId, serverUnits)
            .map { WeatherResponseListMapper(placeId, serverUnits).map(it) }
            .zipWith(fetchForecast(placeId)) { currentWeatherEntry, sunsetAndForecastWeathers ->
                val (sunset, forecastWeathers) = sunsetAndForecastWeathers
                Pair(
                    listOf(sunset),
                    mutableListOf(currentWeatherEntry).apply { addAll(forecastWeathers) })
            }
    }


    private fun fetchWeatherAsync(id: Int, serverUnits: Units): Single<WeatherResponse> {
        return appPrefs.getServerLanguage()
            .flatMap { language ->
                api.getWeatherAsync(BuildConfig.APP_ID, id, language, serverUnits.serverValue)
            }
    }


    private fun fetchForecast(placeId: Int): Single<Pair<SunsetSunriseTimezonePlaceEntry, List<WeatherEntry>>> {
        val serverUnits = appPrefs.getServerAPIUnits()
        return appPrefs
            .getServerLanguage()
            .flatMap { language ->
                api.getForecastAsync(
                    BuildConfig.APP_ID,
                    placeId,
                    lang = language,
                    units = serverUnits.serverValue
                )
            }
            .map { forecast ->
                val updatePlaceModel = PlaceListMapper().map(forecast.city)
                val weathers =
                    ForecastWeatherListMapper(placeId, serverUnits).map(forecast.list)
                Pair(updatePlaceModel, weathers)
            }
    }


    fun getCurrentWeatherWithForecast(): Observable<List<WeatherWithPlace>> {
        val minus30Minutes = DateBuilder(Date()).minusMinutes(30).build()
        val plus5Days = DateBuilder(Date()).plusDays(5).build()
        return weatherDAO.getDetailedCurrentWeather(minus30Minutes, plus5Days).toObservable()
    }

    fun getCurrentPlaceSunsetAndSunrise(): Observable<SunsetSunriseTimezonePlaceEntry> =
        placeDAO.getCurrentSunsetSunriseInfo().toObservable()

    fun getCurrentWeatherSingle(): Single<WeatherWithPlace> {
        val now = DateBuilder(Date()).build()
        return weatherDAO.getCurrentWeather(now).firstOrError()
    }

    fun fetchAndSaveAllPlacesCurrentWeathers(): Completable {
        val fetchCurrentCityForecast =
            placeDAO.loadCurrentPlaceId().flatMap { fetchCompleteForecast(it) }
        return fetchCurrentCityForecast
            .mergeWith(fetchLikedPlacesCurrentWeathers())
            .flatMapCompletable {
                it.save()
            }
    }

    private fun fetchLikedPlacesCurrentWeathers(): Single<Pair<List<SunsetSunriseTimezonePlaceEntry>, List<WeatherEntry>>> {
        return placeDAO.loadLikedPlaceIds()
            .flatMap { ids ->
                val requests =
                    ids.map { zipCurrentWeatherWithPlaceId(it, appPrefs.getServerAPIUnits()) }
                Single.zip(requests) { it }
                    .filter { it.first() is PlaceResponse }
                    .toSingle(emptyArray<PlaceResponse>())
                    .map {
                        val placeResponses =
                            it.mapNotNull { response -> response as? PlaceResponse }
                        mapResponsesToWeatherEntities(placeResponses, appPrefs.getServerAPIUnits())
                    }
            }
    }


    private fun Pair<List<SunsetSunriseTimezonePlaceEntry>, List<WeatherEntry>>.save(): Completable {
        val (sunset, weatherEntries) = this
        return Completable.fromAction {
            placeDAO.updateSunrisesAndSunset(sunset)
            weatherDAO.updateCurrentWeathers(weatherEntries)
            val previousDay = DateBuilder().previousDay().build()
            weatherDAO.deletePreviousEntries(previousDay)
        }
    }


    private fun mapResponsesToWeatherEntities(
        responses: List<PlaceResponse>,
        serverUnits: Units
    ): Pair<List<SunsetSunriseTimezonePlaceEntry>, List<WeatherEntry>> {
        val weathers = responses.map {
            val (id, weather) = it
            WeatherResponseListMapper(id, serverUnits).map(weather)
        }
        val sunsets = responses.map {
            val (id, weather) = it
            PlaceListMapper().map(
                CityResponse(
                    id = id,
                    sunrise = weather.sys?.sunrise ?: 0L,
                    sunset = weather.sys?.sunset ?: 0L,
                    timezone = weather.timezone
                )
            )
        }.distinctBy { it.id }
        return Pair(sunsets, weathers)
    }

    //
    private fun zipCurrentWeatherWithPlaceId(
        placeId: Int,
        serverUnits: Units
    ): Single<PlaceResponse> =
        fetchWeatherAsync(placeId, serverUnits).map { forecast -> PlaceResponse(placeId, forecast) }

    fun getAvailableDaysForPlace(placeId: Int): Flowable<Triple<Int, String, List<Date>>> {
        val almostNow = DateBuilder(Date()).build()
        return weatherDAO
            .getAllWeathersForCity(placeId, almostNow)
            .filter { weathers -> !weathers.isNullOrEmpty() }
            .map { weathers ->
                val timeZone =
                    if (weathers.isNullOrEmpty()) "Europe/Moscow" else weathers[0].timezone
                val dates = weathers.distinctBy {
                    val day = DateBuilder(it.epochDateMills, timeZone).getDayOfYear()
                    day
                }.map { it.epochDateMills }
                return@map Triple(placeId, timeZone, dates)
            }
    }

    fun getWeathersForPlaceAtDay(
        placeId: Int,
        date: Date,
        timeZone: String
    ): Single<List<WeatherWithPlace>> {
        val dayStart = DateBuilder(date, timeZone).endOfNight().build()
        val dayEnd = DateBuilder(date, timeZone).nextDay().endOfNight().build()
        return weatherDAO.getDayWeathersForCity(placeId, dayStart, dayEnd)
    }
}

data class PlaceResponse(val id: Int, val weatherResponse: WeatherResponse)