package com.lab.esh1n.weather.domain.places

import android.content.res.AssetManager
import androidx.paging.PagedList
import androidx.paging.toObservable
import com.esh1n.utils_android.DateBuilder
import com.esh1n.utils_android.FileReader
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lab.esh1n.weather.BuildConfig
import com.lab.esh1n.weather.data.api.APIService
import com.lab.esh1n.weather.data.api.response.PlaceAsset
import com.lab.esh1n.weather.data.cache.AppPrefs
import com.lab.esh1n.weather.data.cache.WeatherDB
import com.lab.esh1n.weather.data.cache.entity.PlaceWithCurrentWeatherEntry
import com.lab.esh1n.weather.domain.ProgressModel
import com.lab.esh1n.weather.domain.places.mapper.PlaceEntryMapper
import com.lab.esh1n.weather.domain.weather.mapper.ForecastWeatherListMapper
import com.lab.esh1n.weather.domain.weather.mapper.PlaceListMapper
import io.reactivex.*
import java.lang.reflect.Type
import java.util.*


class PlacesRepository constructor(private val apiService: APIService, db: WeatherDB,
                                   private val appPrefs: AppPrefs, private val assetManager: AssetManager) {
    private val placeDAO = db.placeDAO()
    private val weatherDAO = db.weatherDAO()

    fun getIsPlaceFavourite(placeId: Int) = placeDAO.getIsPlaceFavourite(placeId)

    suspend fun changeFavouriteState(placeId: Int, isLiked: Boolean) =
        placeDAO.changeFavouriteState(placeId, isLiked)

    fun searchPlaces(query: String): Observable<PagedList<PlaceWithCurrentWeatherEntry>> {
        val now = DateBuilder(Date()).build()
        return placeDAO.searchPlacesWithCurrentWeather(now, query).toObservable(pageSize = 20)
    }

    fun checkIfCurrentPlaceExist(): Single<Boolean> {
        return placeDAO.checkIfCurrentPlaceExist()
    }

    fun updateCurrentPlacesForecast(): Completable {
        val threeHoursAgo = DateBuilder(Date()).minusHours(3).build()
        val lang = appPrefs.getLocale().language
        val serverUnits = appPrefs.getServerAPIUnits()
        return weatherDAO
                .clearOldWeathers(threeHoursAgo)
                .andThen(placeDAO.getCurrentCityId()
                        .flatMap { id ->
                            apiService.getForecastAsync(appId = BuildConfig.APP_ID, id = id, lang = lang, units = serverUnits.serverValue)
                        }
                        .map { response ->
                            val id = response.city!!.id!!
                            val updatePlaceModel = PlaceListMapper().map(response.city!!)
                            val weathers = ForecastWeatherListMapper(id, serverUnits).map(response.list)
                            return@map Pair(updatePlaceModel, weathers)
                        }
                        .flatMapCompletable { placeAndWeathers ->
                            val updatePlaceEntry = placeAndWeathers.first
                            placeDAO.updateSunsetSunrise(updatePlaceEntry.id, updatePlaceEntry.timezone, updatePlaceEntry.sunrise, updatePlaceEntry.sunset)
                            weatherDAO.saveWeathersCompletable(placeAndWeathers.second)
                        }
                )
    }

    fun prePopulatePlaces(): Flowable<ProgressModel<Unit>> =
            Flowable.create({ emitter ->
                val stream = assetManager.open("city.list.json")
                val readFromAssetPart = 45
                val citiesJSON = FileReader.readFileToString3(stream, { relation ->
                    val percent = (readFromAssetPart * relation).toInt()
                    emitter.onNext(ProgressModel(percent, "reading String from assets"))
                })

                val cityType: Type = object : TypeToken<List<PlaceAsset>>() {}.type
                val places = Gson().fromJson<List<PlaceAsset>>(citiesJSON, cityType)
                val percentAfterGSONMapping = 50

                emitter.onNext(ProgressModel(percentAfterGSONMapping, "mapped string to JSON "))
                val mappingToEntriesPart = 40
                val placeEntries = PlaceEntryMapper()
                        .map(places,
                                { relation ->
                                    val percent = (percentAfterGSONMapping + mappingToEntriesPart * relation).toInt()
                                    emitter.onNext(ProgressModel(percent, "mapped json to entries "))
                                })
                emitter.onNext(ProgressModel(90, "start write to database"))
                placeDAO.insertPlaces(placeEntries)
                stream.close()
                emitter.onNext(ProgressModel(100, "written to database"))
                emitter.onComplete()
            }, BackpressureStrategy.BUFFER)


    fun updateCurrentPlace(id: Int): Completable {
        return Completable.fromAction {
            placeDAO.updateCurrentPlace(id)
        }
    }
}