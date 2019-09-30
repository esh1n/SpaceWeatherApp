package com.lab.esh1n.weather.weather.viewmodel

import android.app.Application
import com.esh1n.core_android.rx.applyAndroidSchedulers
import com.esh1n.core_android.ui.viewmodel.BaseViewModel
import com.esh1n.core_android.ui.viewmodel.Resource
import com.esh1n.core_android.ui.viewmodel.SingleLiveEvent
import com.lab.esh1n.weather.domain.weather.places.usecase.CheckDataInitializedUseCase
import javax.inject.Inject

class RouteVM @Inject constructor(app: Application, private val checkDataInitializedUseCase: CheckDataInitializedUseCase) : BaseViewModel(app) {

    val dataWasInitializedEvent = SingleLiveEvent<Resource<Boolean>>()

    fun checkIfInitialized() {
        addDisposable(
                checkDataInitializedUseCase.perform(Unit)
                        .applyAndroidSchedulers()
                        .subscribe { models ->
                            dataWasInitializedEvent.postValue(models)
                        }
        )
    }
}