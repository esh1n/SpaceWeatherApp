package com.lab.esh1n.weather.weather.viewmodel

import androidx.lifecycle.*
import androidx.work.WorkManager
import com.lab.esh1n.weather.domain.base.Resource
import com.lab.esh1n.weather.domain.weather.FetchAndSaveWeatherUseCase
import com.lab.esh1n.weather.domain.weather.LoadWeatherByCityFromDBUseCase
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
        val mappedWeather = Transformations
                .map(loadEventsUseCase.execute(CITY_NAME))
                {
                    Resource.map(it, cityWeatherModelMapper::map)
                }
        emitSource(mappedWeather)
        refresh()
    }


    fun refresh() {
        viewModelScope.launch() {
            val result = withContext(Dispatchers.IO) { fetchAndSaveWeatherUseCase.execute(CITY_NAME) }
            refreshOperation.postValue(result)
        }
    }

    companion object {
        const val CITY_NAME = "Saint Petersburg"
    }

}