package com.lab.esh1n.weather.domain.weather.places.usecase

import com.esh1n.core_android.error.ErrorsHandler
import com.esh1n.core_android.ui.viewmodel.Resource
import com.lab.esh1n.weather.domain.weather.ProgressModel
import com.lab.esh1n.weather.domain.weather.UseCase
import com.lab.esh1n.weather.domain.weather.places.PlacesRepository
import io.reactivex.Flowable


class PrePopulatePlacesUseCase(private val placesRepository: PlacesRepository, errorsHandler: ErrorsHandler) :
        UseCase<Flowable<Resource<ProgressModel<Unit>>>, Unit>(errorsHandler) {

    override fun perform(args: Unit): Flowable<Resource<ProgressModel<Unit>>> {
        return placesRepository
                .prePopulatePlaces()
                .map {
                    Resource.success(it)
                }
                .onErrorReturn { error ->
                    Resource.error(errorsHandler.handle(error))
                }
    }

}