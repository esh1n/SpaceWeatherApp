package com.lab.esh1n.weather.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.work.WorkManager
import com.lab.esh1n.weather.base.BaseViewModel
import com.lab.esh1n.weather.domain.base.Resource
import com.lab.esh1n.weather.domain.events.FetchAndSaveEventsUseCase
import com.lab.esh1n.weather.domain.events.GetEventsInDBUseCase
import com.lab.esh1n.weather.weather.WeatherModel
import com.lab.esh1n.weather.weather.mapper.EventModelMapper
import com.lab.esh1n.weather.utils.SingleLiveEvent
import com.lab.esh1n.weather.utils.applyAndroidSchedulers
import com.lab.esh1n.weather.utils.startPeriodicSync
import javax.inject.Inject

/**
 * Created by esh1n on 3/16/18.
 */

class EventsViewModel
@Inject
constructor(private val loadEventsUseCase: GetEventsInDBUseCase,
            private val fetchAndSaveEventsUseCase: FetchAndSaveEventsUseCase,
            private val workManager: WorkManager)
    : BaseViewModel() {

    val events = MutableLiveData<Resource<List<WeatherModel>>>()
    val refreshOperation = SingleLiveEvent<Resource<Unit>>()
    private val eventModelMapper = EventModelMapper()

    //TODO move this periodic sync to success login event
    fun startPeriodicSync() {
        workManager.startPeriodicSync()
    }

    fun loadEvents() {
        events.postValue(Resource.loading())
        addDisposable(
                loadEventsUseCase.execute(Unit)
                        .map { return@map Resource.map(it, eventModelMapper::map) }
                        .applyAndroidSchedulers()
                        .subscribe { models -> events.postValue(models) })
    }

    fun refresh() {
        addDisposable(
                fetchAndSaveEventsUseCase.execute(Unit)
                        .doOnSubscribe { _ ->
                            refreshOperation.postValue(Resource.loading())
                        }
                        .applyAndroidSchedulers()
                        .subscribe { result ->
                            refreshOperation.postValue(result)
                        })
    }

}