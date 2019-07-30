package com.lab.esh1n.weather.domain.weather.places

import com.lab.esh1n.data.api.APIService
import com.lab.esh1n.data.cache.WeatherDB
import com.lab.esh1n.data.cache.entity.PlaceEntry
import io.reactivex.Observable

class PlacesRepository constructor(private val apiService: APIService, db: WeatherDB) {
    private val placeDAO = db.placeDAO()

    fun getAllPlaces(): Observable<List<PlaceEntry>> {
        return placeDAO.getAllPlaces().toObservable()
    }
}