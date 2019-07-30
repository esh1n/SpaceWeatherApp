package com.lab.esh1n.weather.domain.weather.places.usecase

import com.esh1n.core_android.error.ErrorsHandler
import com.esh1n.core_android.ui.viewmodel.Resource
import com.lab.esh1n.data.cache.entity.PlaceEntry
import com.lab.esh1n.weather.domain.weather.UseCase
import com.lab.esh1n.weather.domain.weather.places.PlacesRepository
import io.reactivex.Observable

class GetAllPlacesUse(private val placesRepository: PlacesRepository, errorsHandler: ErrorsHandler)
    : UseCase<Observable<Resource<List<PlaceEntry>>>, Unit>(errorsHandler) {
    override fun perform(args: Unit): Observable<Resource<List<PlaceEntry>>> {
        return placesRepository.getAllPlaces().map {
            Resource.success(it)
        }.onErrorReturn { error ->
            Resource.error(errorsHandler.handle(error))
        }
    }
}