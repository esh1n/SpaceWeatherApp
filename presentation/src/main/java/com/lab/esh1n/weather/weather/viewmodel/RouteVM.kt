package com.lab.esh1n.weather.weather.viewmodel

import com.esh1n.core_android.rx.applyAndroidSchedulers

import com.esh1n.core_android.ui.viewmodel.AutoClearViewModel
import com.esh1n.core_android.ui.viewmodel.SingleLiveEvent
import com.lab.esh1n.weather.domain.places.usecase.CheckDataInitializedUseCase
import javax.inject.Inject

class RouteVM @Inject constructor(private val checkDataInitializedUseCase: CheckDataInitializedUseCase) :
    AutoClearViewModel() {

    val dataWasInitializedEvent = SingleLiveEvent<Boolean>()

    fun checkIfInitialized() {
        checkDataInitializedUseCase.perform(Unit)
            .applyAndroidSchedulers()
            .subscribe(dataWasInitializedEvent::postValue)
            .disposeOnDestroy()
    }
}