package com.lab.esh1n.weather.weather.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.work.WorkManager
import com.esh1n.core_android.error.ErrorModel
import com.esh1n.core_android.rx.SchedulersFacade
import com.esh1n.core_android.rx.applyAndroidSchedulers
import com.esh1n.core_android.ui.viewmodel.BaseViewModel
import com.esh1n.core_android.ui.viewmodel.Resource
import com.esh1n.core_android.ui.viewmodel.SingleLiveEvent
import com.lab.esh1n.weather.domain.weather.weather.usecases.FetchAndSaveCurrentPlaceWeatherUseCase
import com.lab.esh1n.weather.domain.weather.weather.usecases.LoadCurrentWeatherLiveDataUseCase
import com.lab.esh1n.weather.utils.startPeriodicSync
import com.lab.esh1n.weather.weather.WeatherModel
import com.lab.esh1n.weather.weather.mapper.WeatherModelMapper
import javax.inject.Inject

/**
 * Created by esh1n on 3/16/18.
 */

class CurrentWeatherVM
@Inject
constructor(private val loadCurrentWeatherUseCase: LoadCurrentWeatherLiveDataUseCase,
            private val fetchAndSaveWeatherUseCase: FetchAndSaveCurrentPlaceWeatherUseCase,
            application: Application,
            private val workManager: WorkManager)
    : BaseViewModel(application) {

    val refreshOperation = SingleLiveEvent<Resource<Unit>>()
    val weatherLiveData = MutableLiveData<Resource<WeatherModel>>()
    private val cityWeatherModelMapper = WeatherModelMapper()

    //TODO move this periodic sync to success login event
    fun startPeriodicSync() {
        workManager.startPeriodicSync()
    }


    fun loadWeather() {
        weatherLiveData.postValue(Resource.loading())
        addDisposable(
                loadCurrentWeatherUseCase.perform(Unit)
                        .map { return@map Resource.map(it, cityWeatherModelMapper::map) }
                        .compose(SchedulersFacade.applySchedulersObservable())
                        .subscribe({ models -> weatherLiveData.postValue(models) },
                                { throwable ->
                                    weatherLiveData.postValue(Resource.error(ErrorModel.unexpectedError(throwable.message
                                            ?: "")))
                                })
        )
        refresh()
    }


    fun refresh() {
        addDisposable(
                fetchAndSaveWeatherUseCase.perform(Unit)
                        .doOnSubscribe { _ ->
                            refreshOperation.postValue(Resource.loading())
                        }
                        .applyAndroidSchedulers()
                        .subscribe({ result -> refreshOperation.postValue(result) },
                                { throwable ->
                                    refreshOperation.postValue(Resource.error(ErrorModel.unexpectedError(throwable.message
                                            ?: "")))
                                })
        )
    }

}