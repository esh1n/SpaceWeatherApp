package com.lab.esh1n.weather.domain.weather.places.usecase

import androidx.paging.PagedList
import com.esh1n.core_android.error.ErrorsHandler
import com.esh1n.core_android.ui.viewmodel.Resource
import com.lab.esh1n.data.cache.entity.PlaceWithCurrentWeatherEntry
import com.lab.esh1n.weather.domain.weather.UseCase
import com.lab.esh1n.weather.domain.weather.places.PlacesRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetAllPlacesUse @Inject constructor(private val placesRepository: PlacesRepository, errorsHandler: ErrorsHandler)
    : UseCase<Observable<Resource<PagedList<PlaceWithCurrentWeatherEntry>>>, Unit>(errorsHandler) {
    override fun perform(args: Unit): Observable<Resource<PagedList<PlaceWithCurrentWeatherEntry>>> {
        return placesRepository.getAllPlaces().map {
            Resource.success(it)
        }.onErrorReturn { error ->
            Resource.error(errorsHandler.handle(error))
        }
    }
}