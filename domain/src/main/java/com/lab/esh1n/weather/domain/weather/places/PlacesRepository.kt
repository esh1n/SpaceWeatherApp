package com.lab.esh1n.weather.domain.weather.places

import android.content.res.AssetManager
import androidx.paging.PagedList
import androidx.paging.toObservable
import com.esh1n.utils_android.DateBuilder
import com.esh1n.utils_android.FileReader
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lab.esh1n.data.api.APIService
import com.lab.esh1n.data.api.response.PlaceAsset
import com.lab.esh1n.data.cache.AppPrefs
import com.lab.esh1n.data.cache.WeatherDB
import com.lab.esh1n.data.cache.entity.PlaceWithCurrentWeatherEntry
import com.lab.esh1n.weather.domain.BuildConfig
import com.lab.esh1n.weather.domain.weather.places.mapper.PlaceEntryMapper
import com.lab.esh1n.weather.domain.weather.weather.mapper.ForecastWeatherListMapper
import com.lab.esh1n.weather.domain.weather.weather.mapper.PlaceListMapper
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.lang.reflect.Type
import java.util.*


class PlacesRepository constructor(private val apiService: APIService, db: WeatherDB,
                                   private val appPrefs: AppPrefs, private val assetManager: AssetManager) {
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
        return placeDAO.getPlaceIdsToSync()
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
                    placeDAO.updateSunsetSunrise(updatePlaceEntry.id, updatePlaceEntry.timezone, updatePlaceEntry.sunrise, updatePlaceEntry.sunset)
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
                            val updatePlaceModel = PlaceListMapper().map(response.city!!)
                            val weathers = ForecastWeatherListMapper(id).map(response.list)
                            return@map Pair(updatePlaceModel, weathers)
                        }
                        .flatMapCompletable { placeAndWeathers ->
                            val updatePlaceEntry = placeAndWeathers.first
                            placeDAO.updateSunsetSunrise(updatePlaceEntry.id, updatePlaceEntry.timezone, updatePlaceEntry.sunrise, updatePlaceEntry.sunset)
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
        return Single.fromCallable {
            val inputStream = assetManager.open("city.list.json")
            val citiesJSON = FileReader.readFileToString3(inputStream)
            inputStream.close()
            val cityType: Type = object : TypeToken<List<PlaceAsset>>() {}.type
            val places = Gson().fromJson<List<PlaceAsset>>(citiesJSON, cityType)
            return@fromCallable PlaceEntryMapper().map(places)
        }.flatMapCompletable { placeEntries ->
            return@flatMapCompletable placeDAO.insertPlaces(placeEntries)
        }


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