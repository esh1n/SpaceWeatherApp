package com.lab.esh1n.weather.domain.weather.places

import androidx.paging.PagedList
import androidx.paging.toObservable
import com.esh1n.utils_android.DateBuilder
import com.lab.esh1n.data.api.APIService
import com.lab.esh1n.data.cache.AppPrefs
import com.lab.esh1n.data.cache.WeatherDB
import com.lab.esh1n.data.cache.entity.PlaceEntry
import com.lab.esh1n.data.cache.entity.PlaceWithCurrentWeatherEntry
import com.lab.esh1n.weather.domain.BuildConfig
import com.lab.esh1n.weather.domain.weather.weather.mapper.EpochDateListMapper
import com.lab.esh1n.weather.domain.weather.weather.mapper.ForecastWeatherListMapper
import com.lab.esh1n.weather.domain.weather.weather.mapper.PlaceListMapper
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.*

class PlacesRepository constructor(private val apiService: APIService, db: WeatherDB, private val appPrefs: AppPrefs) {
    private val placeDAO = db.placeDAO()
    private val weatherDAO = db.weatherDAO()


    fun getAllPlaces(): Observable<PagedList<PlaceWithCurrentWeatherEntry>> {
        val now = Date()
        return placeDAO.getAllPlacesWithCurrentWeather(now).toObservable(pageSize = 20)
    }

    fun checkIfCurrentPlaceExist(): Single<Boolean> {
        return placeDAO.checkIfCurrentPlaceExist()
    }

    fun fetchAndSaveAllPlacesForecast(): Completable {
        return placeDAO.getAllPlacesIds()
                .flattenAsObservable { it }
                .flatMapSingle { id ->
                    val unitsAndLang = appPrefs.getLangAndUnits()
                    apiService.getForecastAsync(BuildConfig.APP_ID, id, unitsAndLang.first, unitsAndLang.second)
                }
                .map { response ->
                    val id = response.city!!.id!!
                    val updatePlaceModel = PlaceListMapper().map(response.city!!)
                    val weathers = ForecastWeatherListMapper(id).map(response.list)
                    return@map Pair(updatePlaceModel, weathers)
                }
                .flatMapCompletable { placeAndWeathers ->
                    val updatePlaceEntry = placeAndWeathers.first
                    placeDAO.updateSunsetSunrise(updatePlaceEntry.id, updatePlaceEntry.sunrise, updatePlaceEntry.sunset)
                    weatherDAO.saveWeathersCompletable(placeAndWeathers.second)
                }
    }

    fun updateCurrentPlacesForecast(): Completable {
        val threeHoursAgo = DateBuilder(Date()).minusHours(3).build()

        return weatherDAO
                .clearOldWeathers(threeHoursAgo)
                .andThen(getIdUnitAndLang()
                        .flatMap { idUnitAndLang ->
                            val id = idUnitAndLang.first
                            val units = idUnitAndLang.second
                            val lang = idUnitAndLang.third
                            apiService.getForecastAsync(BuildConfig.APP_ID, id, units, lang)
                        }
                        .map { response ->
                            val id = response.city!!.id!!
                            val dateConverter = EpochDateListMapper()
                            val updatePlaceModel = PlaceListMapper().map(response.city!!)
                            val weathers = ForecastWeatherListMapper(id).map(response.list)
                            return@map Pair(updatePlaceModel, weathers)
                        }
                        .flatMapCompletable { placeAndWeathers ->
                            val updatePlaceEntry = placeAndWeathers.first
                            placeDAO.updateSunsetSunrise(updatePlaceEntry.id, updatePlaceEntry.sunrise, updatePlaceEntry.sunset)
                            weatherDAO.saveWeathersCompletable(placeAndWeathers.second)
                        }
                )
    }

    private fun getIdUnitAndLang(): Single<Triple<Int, String, String>> {
        return Single.zip(placeDAO.getCurrentCityId(), appPrefs.getLangAndUnitsSingle(), BiFunction { id, unitsAndLang ->
            return@BiFunction Triple(id, unitsAndLang.first, unitsAndLang.second)
        })
    }

    fun prePopulatePlaces(): Completable {
        val now = Date().time
        val PREPOPULATE_PLACES = listOf(
                PlaceEntry(472045, "Воронеж", "Europe/Moscow", true, now, now),
                PlaceEntry(765876, "Люблин", "Europe/Prague", false, now, now),
                // PlaceEntry(6455259, "Париж", "Europe/Prague", false, now, now),
                PlaceEntry(524901, "Москва", "Europe/Moscow", false, now, now),
                PlaceEntry(694423, "Севастополь", "Europe/Moscow", false, now, now),
                PlaceEntry(498817, "Ленинград", "Europe/Moscow", false, now, now),
                PlaceEntry(6356055, "Барса", "Europe/Prague", false, now, now),
                PlaceEntry(3164603, "Венеция", "Europe/Prague", false, now, now),
                PlaceEntry(3067696, "Прага", "Europe/Prague", false, now, now),
                PlaceEntry(745044, "Стамбул", "Europe/Moscow", false, now, now)


        )
        //   val PREPOPULATE_WEATHER = listOf(WeatherEntry(524901, Date(), "", 12.1, 10.1, 18.1, "01d", "clear sky", 120.0, 12.0, 12f, 12f))
        return placeDAO.insertPlaces(PREPOPULATE_PLACES)
    }

    fun updateCurrentPlace(id: Int): Completable {
        return Completable.fromAction {
            placeDAO.updateCurrentPlace(id)
        }
    }

    fun loadCurrentPlaceId(): Single<Int> {
        return placeDAO.loadCurrentPlaceId()
    }

}