package com.lab.esh1n.weather.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lab.esh1n.weather.base.BaseViewModel
import com.lab.esh1n.weather.domain.base.Resource
import com.lab.esh1n.weather.domain.events.GetEventUseCase
import com.lab.esh1n.weather.weather.WeatherModel
import com.lab.esh1n.weather.weather.mapper.WeatherModelMapper
import javax.inject.Inject


class EventDetailViewModel
@Inject
constructor(private val loadEventUseCase: GetEventUseCase) : BaseViewModel() {

    val event = MutableLiveData<Resource<WeatherModel>>()
    private val eventModelMapper = WeatherModelMapper()

    fun loadEvent(id: Long) {
        event.postValue(Resource.loading())
        addDisposable(
                loadEventUseCase.execute(id)
                        .map { return@map Resource.map(it, eventModelMapper::map) }
                        .applyAndroidSchedulers()
                        .subscribe { models
                            ->
                            event.postValue(models)
                        })
    }

}