package com.lab.esh1n.weather.weather.viewmodel

import android.app.Application
import com.esh1n.core_android.ui.viewmodel.BaseViewModel
import com.esh1n.core_android.ui.viewmodel.Resource
import com.esh1n.core_android.ui.viewmodel.SingleLiveEvent
import com.lab.esh1n.weather.domain.weather.weather.usecases.LoadPlaceAvailableForecastDaysUseCase
import com.lab.esh1n.weather.weather.model.ForecastDayModel
import javax.inject.Inject

class ForecastWeekVM @Inject constructor(loadForecastUsecase: LoadPlaceAvailableForecastDaysUseCase, app: Application) : BaseViewModel(app) {

    val availableDays = SingleLiveEvent<Resource<List<ForecastDayModel>>>()

    fun loadAvailableDays(){

    }
}