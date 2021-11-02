package com.lab.esh1n.weather.weather.viewmodel

import android.app.Application
import com.esh1n.core_android.rx.applyAndroidSchedulers
import com.esh1n.core_android.ui.viewmodel.BaseAndroidViewModel
import com.esh1n.core_android.ui.viewmodel.Resource
import com.esh1n.core_android.ui.viewmodel.SingleLiveEvent
import com.lab.esh1n.weather.domain.places.usecase.CheckDataInitializedUseCase
import javax.inject.Inject

class RouteVM @Inject constructor(app: Application, private val checkDataInitializedUseCase: CheckDataInitializedUseCase) : BaseAndroidViewModel(app) {

    var testString = ""
    val dataWasInitializedEvent = SingleLiveEvent<Resource<Boolean>>()

    fun checkIfInitialized() {
                checkDataInitializedUseCase.perform(Unit)
                        .applyAndroidSchedulers()
                        .subscribe { models ->
                            testString = "opa"
                            dataWasInitializedEvent.postValue(models)
                        }.disposeOnDestroy()
    }
}