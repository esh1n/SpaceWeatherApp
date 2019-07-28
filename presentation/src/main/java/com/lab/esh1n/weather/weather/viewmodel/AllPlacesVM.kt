package com.lab.esh1n.weather.weather.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.esh1n.core_android.rx.SchedulersFacade
import com.esh1n.core_android.ui.viewmodel.BaseViewModel
import com.esh1n.core_android.ui.viewmodel.Resource
import com.esh1n.core_android.ui.viewmodel.SingleLiveEvent
import com.lab.esh1n.weather.domain.weather.weather.repository.WeatherRepository
import com.lab.esh1n.weather.weather.model.PlaceWeather
import javax.inject.Inject

class AllPlacesVM @Inject constructor(private val weatherRepository: WeatherRepository, application: Application) : BaseViewModel(application) {

    val updateCurrentPlaceOperation = SingleLiveEvent<Resource<Unit>>()

    fun saveCurrentPlace(cityName: String) {
        updateCurrentPlaceOperation.postValue(Resource.loading())

        addDisposable(
                weatherRepository.saveCurrentCity(cityName)
                        .compose(SchedulersFacade.applySchedulersCompletable())
                        .subscribe({ updateCurrentPlaceOperation.call() },
                                {
                                    updateCurrentPlaceOperation.postValue(Resource.error(it))
                                })
        )

    }

    val allCities = MutableLiveData<Resource<List<PlaceWeather>>>().apply {
        value = Resource.success(listOf(PlaceWeather("Voronezh"), PlaceWeather("Moscow"),
                PlaceWeather("Venice"), PlaceWeather("Istanbul"),
                PlaceWeather("Barcelona")))
    }

}