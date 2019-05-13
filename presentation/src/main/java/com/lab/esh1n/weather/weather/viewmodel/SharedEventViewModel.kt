package com.lab.esh1n.weather.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedEventViewModel : ViewModel() {
    val eventId = MutableLiveData<Long?>()
}
