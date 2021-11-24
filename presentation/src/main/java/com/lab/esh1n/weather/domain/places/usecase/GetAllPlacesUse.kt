package com.lab.esh1n.weather.domain.places.usecase

import androidx.paging.PagedList
import com.esh1n.core_android.error.ErrorsHandler
import com.esh1n.core_android.ui.viewmodel.Resource
import com.lab.esh1n.weather.data.cache.entity.PlaceWithCurrentWeatherEntry
import com.lab.esh1n.weather.domain.UseCase
import com.lab.esh1n.weather.domain.places.PlacesRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetAllPlacesUse @Inject constructor(private val placesRepository: PlacesRepository, errorsHandler: ErrorsHandler)
    : UseCase<Observable<Resource<PagedList<PlaceWithCurrentWeatherEntry>>>, String>(errorsHandler) {
    override fun perform(args: String): Observable<Resource<PagedList<PlaceWithCurrentWeatherEntry>>> {
        return placesRepository.searchPlaces(args)
            .map { places -> Resource.success(places) }
                .onErrorReturn { error -> Resource.error(errorsHandler.handle(error)) }
    }
}