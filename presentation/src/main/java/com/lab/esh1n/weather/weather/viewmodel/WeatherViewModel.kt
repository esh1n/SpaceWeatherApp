package com.lab.esh1n.weather.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.lab.esh1n.weather.domain.base.Resource
import com.lab.esh1n.weather.domain.weather.usecases.FetchAndSaveWeatherUseCase
import com.lab.esh1n.weather.domain.weather.usecases.LoadWeatherByCityFromDBUseCase
import com.lab.esh1n.weather.utils.SingleLiveEvent
import com.lab.esh1n.weather.utils.startPeriodicSync
import com.lab.esh1n.weather.weather.WeatherModel
import com.lab.esh1n.weather.weather.mapper.WeatherModelMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by esh1n on 3/16/18.
 */

class WeatherViewModel
@Inject
constructor(private val loadEventsUseCase: LoadWeatherByCityFromDBUseCase,
            private val fetchAndSaveWeatherUseCase: FetchAndSaveWeatherUseCase,
            private val workManager: WorkManager)
    : ViewModel() {

    val refreshOperation = SingleLiveEvent<Resource<Unit>>()
    private val cityWeatherModelMapper = WeatherModelMapper()

    //TODO move this periodic sync to success login event
    fun startPeriodicSync() {
        workManager.startPeriodicSync()
    }

    val weatherLiveData: LiveData<Resource<WeatherModel>> = liveData() {
        val weatherEntity = loadEventsUseCase.execute(CITY_NAME)
        val mappedResource = Resource.map(weatherEntity, cityWeatherModelMapper::map)
        emit(mappedResource)
        refresh()
    }


    fun refresh() {
        viewModelScope.launch() {
            val result = withContext(Dispatchers.IO) { fetchAndSaveWeatherUseCase.execute(CITY_NAME) }
            refreshOperation.postValue(result)
        }
    }

    companion object {
        const val CITY_NAME = "Voronezh"
    }
}