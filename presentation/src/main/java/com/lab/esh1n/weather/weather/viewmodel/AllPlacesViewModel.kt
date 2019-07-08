package com.lab.esh1n.weather.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab.esh1n.weather.domain.base.Resource
import com.lab.esh1n.weather.domain.weather.WeatherRepository
import com.lab.esh1n.weather.utils.SingleLiveEvent
import com.lab.esh1n.weather.weather.model.PlaceWeather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AllPlacesViewModel @Inject constructor(private val weatherRepository: WeatherRepository) : ViewModel() {

    val updateCurrentPlaceOperation = SingleLiveEvent<Resource<Unit>>()

    fun saveCurrentPlace(cityName: String) {
        viewModelScope.launch() {
            val result = withContext(Dispatchers.IO) { weatherRepository.saveCurrentCity(cityName) }
            updateCurrentPlaceOperation.call()
        }
    }

    val allCities = MutableLiveData<Resource<List<PlaceWeather>>>().apply {
        value = Resource.success(listOf(PlaceWeather("Voronezh"), PlaceWeather("Moscow"),
                PlaceWeather("Venice"), PlaceWeather("Istanbul"),
                PlaceWeather("Barcelona")))
    }

}