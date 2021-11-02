package com.lab.esh1n.weather.domain.places.usecase

import com.esh1n.core_android.error.ErrorsHandler
import com.esh1n.core_android.ui.viewmodel.Resource
import com.lab.esh1n.weather.domain.UseCase
import com.lab.esh1n.weather.domain.places.PlacesRepository
import io.reactivex.Single


class DailyForecastSyncUseCase(private val placesRepository: PlacesRepository, errorsHandler: ErrorsHandler) :
        UseCase<Single<Resource<Unit>>, Unit>(errorsHandler) {

    override fun perform(args: Unit): Single<Resource<Unit>> {
        return placesRepository
                .updateCurrentPlacesForecast()
                .andThen(
                        Single.just(Resource.success()))
                .onErrorReturn { error ->
                    Resource.error(errorsHandler.handle(error))
                }
    }

}
