package com.lab.esh1n.weather.domain.weather.places

import com.lab.esh1n.data.api.APIService
import com.lab.esh1n.data.cache.WeatherDB
import com.lab.esh1n.data.cache.entity.PlaceWithCurrentWeatherEntry
import io.reactivex.Completable
import io.reactivex.Observable
import java.util.*

class PlacesRepository constructor(private val apiService: APIService, db: WeatherDB) {
    private val placeDAO = db.placeDAO()

    fun getAllPlaces(): Observable<List<PlaceWithCurrentWeatherEntry>> {
        val now = Date()
        return placeDAO.getAllPlacesWithCurrentWeather(now).toObservable()
    }

    fun updateCurrentPlace(id: Int): Completable {
        return Completable.fromAction {
            placeDAO.updateCurrentPlace(id)
        }
    }
}