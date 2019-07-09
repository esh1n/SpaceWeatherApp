package com.lab.esh1n.weather.weather.viewmodel

import androidx.lifecycle.*
import androidx.work.WorkManager
import com.lab.esh1n.weather.domain.base.Resource
import com.lab.esh1n.weather.domain.weather.usecases.FetchAndSaveCurrentPlaceWeatherUseCase
import com.lab.esh1n.weather.domain.weather.usecases.LoadCurrentWeatherLiveDataUseCase
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

class CurrentWeatherVM
@Inject
constructor(private val loadCurrentWeatherUseCase: LoadCurrentWeatherLiveDataUseCase,
            private val fetchAndSaveWeatherUseCase: FetchAndSaveCurrentPlaceWeatherUseCase,
            private val workManager: WorkManager)
    : ViewModel() {

    val refreshOperation = SingleLiveEvent<Resource<Unit>>()
    private val cityWeatherModelMapper = WeatherModelMapper()

    //TODO move this periodic sync to success login event
    fun startPeriodicSync() {
        workManager.startPeriodicSync()
    }

    val weatherLiveData: LiveData<Resource<WeatherModel>> = liveData() {
        val mappedWeather = Transformations
                .map(loadCurrentWeatherUseCase.execute(Unit))
                {
                    Resource.map(it, cityWeatherModelMapper::map)
                }
        emitSource(mappedWeather)
        refresh()
    }


    fun refresh() {
        viewModelScope.launch() {
            val result = withContext(Dispatchers.IO) { fetchAndSaveWeatherUseCase.execute(Unit) }
            refreshOperation.postValue(result)
        }
    }

}