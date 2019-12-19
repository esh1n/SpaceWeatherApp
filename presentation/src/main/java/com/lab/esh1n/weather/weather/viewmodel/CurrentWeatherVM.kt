package com.lab.esh1n.weather.weather.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.esh1n.core_android.rx.SchedulersFacade
import com.esh1n.core_android.rx.applyAndroidSchedulers
import com.esh1n.core_android.ui.viewmodel.BaseAndroidViewModel
import com.esh1n.core_android.ui.viewmodel.Resource
import com.esh1n.core_android.ui.viewmodel.SingleLiveEvent
import com.lab.esh1n.data.cache.entity.UpdatePlaceEntry
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.domain.weather.weather.usecases.FetchAndSaveCurrentPlaceWeatherUseCase
import com.lab.esh1n.weather.domain.weather.weather.usecases.LoadCurrentWeatherSingleUseCase
import com.lab.esh1n.weather.domain.weather.weather.usecases.LoadCurrentWeatherUseCase
import com.lab.esh1n.weather.utils.NotificationUtil
import com.lab.esh1n.weather.weather.mapper.UiLocalizer
import com.lab.esh1n.weather.weather.mapper.WeatherModelMapper
import com.lab.esh1n.weather.weather.model.WeatherModel
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by esh1n on 3/16/18.
 */

class CurrentWeatherVM
@Inject
constructor(private val loadCurrentWeatherUseCase: LoadCurrentWeatherUseCase,
            private val loadCurrentWeatherSingleUseCase: LoadCurrentWeatherSingleUseCase,
            private val fetchAndSaveWeatherUseCase: FetchAndSaveCurrentPlaceWeatherUseCase,
            private val uiLocalizer: UiLocalizer, application: Application)
    : BaseAndroidViewModel(application) {

    val refreshOperation = SingleLiveEvent<Resource<Unit>>()
    val initAdEvent = SingleLiveEvent<Resource<Boolean>>()
    private val weatherLiveData = MutableLiveData<Resource<List<WeatherModel>>>()
    private val cityWeatherModelMapper = WeatherModelMapper(uiLocalizer)

    //TODO move this periodic sync to success login event

    fun getWeatherLiveData() = weatherLiveData

    fun mapWeatherDataResource(arg: Resource<Pair<UpdatePlaceEntry, List<WeatherWithPlace>>>): Resource<List<WeatherModel>> {
        return Resource.map(arg, cityWeatherModelMapper::map);
    }

    fun loadWeather() {
        //think about if no results how not to show progress
        loadCurrentWeatherUseCase.perform(Unit)
                .throttleFirst(500, TimeUnit.MILLISECONDS, Schedulers.computation())
//                        .doOnSubscribe { _ ->
//                            weatherLiveData.postValue(Resource.loading())
//                        }
                .map {
                    return@map mapWeatherDataResource(it)
                }
                .defaultIfEmpty(
                        Resource.ended()
                )
                .compose(SchedulersFacade.applySchedulersObservable())
                .subscribe { models ->
                    weatherLiveData.postValue(models)
                }.disposeOnDestroy()
    }


    fun refresh() {
        fetchAndSaveWeatherUseCase.perform(Unit)
                .flatMap { loadCurrentWeatherSingleUseCase.perform(Unit) }
                .doOnSuccess {
                    NotificationUtil.sendCurrentWeatherNotification(it, getApplication(), uiLocalizer)
                }
                .doOnSubscribe { _ ->
                    refreshOperation.postValue(Resource.loading())
                }.map { Resource.map(it) { Unit } }
                .applyAndroidSchedulers()
                .subscribe({ result -> refreshOperation.postValue(result) },
                        {
                            refreshOperation.postValue(Resource.error(it))
                        })
                .disposeOnDestroy()
    }


}