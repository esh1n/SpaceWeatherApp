package com.lab.esh1n.weather.domain.places.usecase

import com.esh1n.core_android.error.ErrorsHandler
import com.esh1n.core_android.ui.viewmodel.Resource
import com.lab.esh1n.weather.domain.UseCase
import com.lab.esh1n.weather.domain.places.PlacesRepository
import io.reactivex.Single

class CheckDataInitializedUseCase(private val placesRepository: PlacesRepository, errorsHandler: ErrorsHandler)
    : UseCase<Single<Resource<Boolean>>, Unit>(errorsHandler) {
    override fun perform(args: Unit): Single<Resource<Boolean>> {
        return placesRepository.checkIfCurrentPlaceExist().map {
            Resource.success(it)
        }.onErrorReturn {
            Resource.error(errorsHandler.handle(it))
        }
    }
}