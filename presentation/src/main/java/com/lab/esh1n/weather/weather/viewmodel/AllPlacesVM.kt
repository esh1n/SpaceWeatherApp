package com.lab.esh1n.weather.weather.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.work.WorkManager
import com.esh1n.core_android.rx.SchedulersFacade
import com.esh1n.core_android.rx.applyAndroidSchedulers
import com.esh1n.core_android.ui.viewmodel.BaseViewModel
import com.esh1n.core_android.ui.viewmodel.Resource
import com.esh1n.core_android.ui.viewmodel.SingleLiveEvent
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.domain.weather.places.usecase.GetAllPlacesUse
import com.lab.esh1n.weather.domain.weather.places.usecase.UpdateCurrentPlaceUseCase
import com.lab.esh1n.weather.domain.weather.weather.usecases.LoadCurrentWeatherSingleUseCase
import com.lab.esh1n.weather.utils.NotificationUtil
import com.lab.esh1n.weather.weather.mapper.PlaceWeatherMapper
import com.lab.esh1n.weather.weather.mapper.UiLocalizer
import com.lab.esh1n.weather.weather.model.PlaceModel
import javax.inject.Inject

class AllPlacesVM @Inject constructor(private val loadPlacesUseCase: GetAllPlacesUse,
                                      private val loadCurrentPlaceUseCase: LoadCurrentWeatherSingleUseCase,
                                      private var updateCurrentPlaceUseCase: UpdateCurrentPlaceUseCase,
                                      private val workManager: WorkManager, application: Application,
                                      private val uiLocalizer: UiLocalizer) : BaseViewModel(application) {

    val updateCurrentPlaceOperation = SingleLiveEvent<Resource<WeatherWithPlace>>()

    val allCities = MutableLiveData<Resource<List<PlaceModel>>>()

    private val placeWeatherMapper = PlaceWeatherMapper(uiLocalizer)

    fun saveCurrentPlace(id: Int) {
        updateCurrentPlaceOperation.postValue(Resource.loading())

                updateCurrentPlaceUseCase.perform(id)
                        .flatMap {
                            loadCurrentPlaceUseCase.perform(Unit)
                        }
                        .doOnSubscribe { _ ->
                            updateCurrentPlaceOperation.postValue(Resource.loading())
                        }
                        .applyAndroidSchedulers()
                        .subscribe({ result ->

                            NotificationUtil.sendCurrentWeatherNotification(result, getApplication(), uiLocalizer)
                            updateCurrentPlaceOperation.postValue(result)
                        }, {
                            updateCurrentPlaceOperation.postValue(Resource.error(it))
                        })
                        .disposeOnDestroy()
    }

    fun loadPlaces() {
        //think about if no results how not to show progress
        loadPlacesUseCase.perform(Unit)
                .doOnSubscribe { _ ->
                    allCities.postValue(Resource.loading())
                }
                .map { return@map Resource.map(it, placeWeatherMapper::map) }
                .compose(SchedulersFacade.applySchedulersObservable())
                .subscribe { models ->
                    allCities.postValue(models)
                }
                .disposeOnDestroy()
    }


}