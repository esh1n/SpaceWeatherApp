package com.lab.esh1n.weather.domain.weather.places

import com.esh1n.utils_android.DateBuilder
import com.lab.esh1n.data.api.APIService
import com.lab.esh1n.data.cache.WeatherDB
import com.lab.esh1n.data.cache.entity.PlaceEntry
import com.lab.esh1n.data.cache.entity.PlaceWithCurrentWeatherEntry
import com.lab.esh1n.weather.domain.BuildConfig
import com.lab.esh1n.weather.domain.weather.weather.WeatherRepository.Companion.UNITS
import com.lab.esh1n.weather.domain.weather.weather.mapper.ForecastWeatherMapper
import io.reactivex.Completable
import io.reactivex.Observable
import java.util.*

class PlacesRepository constructor(private val apiService: APIService, db: WeatherDB) {
    private val placeDAO = db.placeDAO()
    private val weatherDAO = db.weatherDAO()


    fun getAllPlaces(): Observable<List<PlaceWithCurrentWeatherEntry>> {
        val now = Date()
        return placeDAO.getAllPlacesWithCurrentWeather(now).toObservable()
    }

    fun fetchAndSaveAllPlacesForecast(): Completable {
        return placeDAO.getAllPlacesIds()
                .flattenAsObservable { it }
                .flatMapSingle { id ->
                    apiService.getForecastAsync(BuildConfig.APP_ID, id, UNITS)
                }
                .map { response ->
                    val id = response.city!!.id!!
                    ForecastWeatherMapper(id).map(response.list)
                }
                .flatMapCompletable { weathers -> weatherDAO.saveWeathers(weathers) }
    }

    fun updateCurrentPlacesForecast(): Completable {
        val threeHoursAgo = DateBuilder(Date()).minusHours(3).build()

        return weatherDAO
                .clearOldWeathers(threeHoursAgo)
                .andThen(placeDAO
                        .getCurrentCityId()
                        .flatMap { id -> apiService.getForecastAsync(BuildConfig.APP_ID, id, UNITS) }
                        .map { response ->
                            val id = response.city!!.id!!
                            ForecastWeatherMapper(id).map(response.list)
                        }
                        .flatMapCompletable { weathers -> weatherDAO.saveWeathers(weathers) }
                )
    }

    fun prePopulatePlaces(): Completable {
        val PREPOPULATE_PLACES = listOf(
                PlaceEntry(472045, "Voronezh", "Europe/Moscow", true),
                PlaceEntry(6455259, "Paris", "Europe/Prague", false),
                PlaceEntry(524901, "Moscow", "Europe/Moscow", false),
                PlaceEntry(694423, "Sevastopol", "Europe/Moscow", false),
                PlaceEntry(498817, "Leningrad", "Europe/Moscow", false),
                PlaceEntry(6356055, "Barcelona", "Europe/Prague", false),
                PlaceEntry(3164603, "Venezia", "Europe/Prague", false),
                PlaceEntry(3067696, "Prague", "Europe/Prague", false),
                PlaceEntry(745044, "Istanbul", "Europe/Moscow", false)

        )
        //   val PREPOPULATE_WEATHER = listOf(WeatherEntry(524901, Date(), "", 12.1, 10.1, 18.1, "01d", "clear sky", 120.0, 12.0, 12f, 12f))
        return placeDAO.insertPlaces(PREPOPULATE_PLACES)
    }

    fun updateCurrentPlace(id: Int): Completable {
        return Completable.fromAction {
            placeDAO.updateCurrentPlace(id)
        }
    }
}