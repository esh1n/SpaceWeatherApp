package com.lab.esh1n.weather.weather.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.esh1n.core_android.rx.SchedulersFacade
import com.esh1n.core_android.rx.applyAndroidSchedulers
import com.esh1n.core_android.ui.viewmodel.BaseViewModel
import com.esh1n.core_android.ui.viewmodel.Resource
import com.esh1n.core_android.ui.viewmodel.SingleLiveEvent
import com.lab.esh1n.weather.domain.weather.places.usecase.GetAllPlacesUse
import com.lab.esh1n.weather.domain.weather.places.usecase.UpdateCurrentPlaceUseCase
import com.lab.esh1n.weather.weather.mapper.PlaceWeatherMapper
import com.lab.esh1n.weather.weather.model.PlaceModel
import javax.inject.Inject

class AllPlacesVM @Inject constructor(private val loadPlacesUseCase: GetAllPlacesUse, private var updateCurrentPlaceUseCase: UpdateCurrentPlaceUseCase, application: Application) : BaseViewModel(application) {

    val updateCurrentPlaceOperation = SingleLiveEvent<Resource<Unit>>()

    val allCities = MutableLiveData<Resource<List<PlaceModel>>>()

    private val placeWeatherMapper = PlaceWeatherMapper()

    fun saveCurrentPlace(id: Int) {
        updateCurrentPlaceOperation.postValue(Resource.loading())

        addDisposable(
                updateCurrentPlaceUseCase.perform(id)
                        .doOnSubscribe { _ ->
                            updateCurrentPlaceOperation.postValue(Resource.loading())
                        }
                        .applyAndroidSchedulers()
                        .subscribe({ result -> updateCurrentPlaceOperation.postValue(result) },
                                {
                                    updateCurrentPlaceOperation.postValue(Resource.error(it))
                                })
        )

    }

    fun loadPlaces() {
        //think about if no results how not to show progress
        addDisposable(
                loadPlacesUseCase.perform(Unit)
                        .doOnSubscribe { _ ->
                            allCities.postValue(Resource.loading())
                        }
                        .map { return@map Resource.map(it, placeWeatherMapper::map) }
                        .compose(SchedulersFacade.applySchedulersObservable())
                        .subscribe { models ->
                            allCities.postValue(models)
                        }
        )
    }



}