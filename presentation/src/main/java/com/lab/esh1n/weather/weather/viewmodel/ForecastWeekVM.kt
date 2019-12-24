package com.lab.esh1n.weather.weather.viewmodel

import com.esh1n.core_android.rx.SchedulersFacade
import com.esh1n.core_android.ui.BaseVM
import com.esh1n.core_android.ui.viewmodel.Resource
import com.esh1n.core_android.ui.viewmodel.SingleLiveEvent
import com.lab.esh1n.weather.domain.weather.weather.usecases.LoadPlaceAvailableForecastDaysUseCase
import com.lab.esh1n.weather.weather.mapper.AvailableDaysMapper
import com.lab.esh1n.weather.weather.mapper.UiLocalizer
import com.lab.esh1n.weather.weather.model.ForecastDayModel
import javax.inject.Inject

class ForecastWeekVM @Inject constructor(private val loadForecastUseCase: LoadPlaceAvailableForecastDaysUseCase, uiLocalizer: UiLocalizer) : BaseVM() {

    val availableDays = SingleLiveEvent<Resource<List<ForecastDayModel>>>()
    private val availableDaysMapper = AvailableDaysMapper(uiLocalizer)

    fun loadAvailableDays(placeId: Int) {
        loadForecastUseCase
                .perform(placeId)
                .map { availableDays -> Resource.map(availableDays, availableDaysMapper::map) }
                .compose(SchedulersFacade.applySchedulersSingle())
                .subscribe { models -> availableDays.postValue(models) }
                .disposeOnDestroy()
    }
}