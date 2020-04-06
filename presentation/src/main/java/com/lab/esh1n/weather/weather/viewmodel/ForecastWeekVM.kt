package com.lab.esh1n.weather.weather.viewmodel

import com.esh1n.core_android.rx.SchedulersFacade
import com.esh1n.core_android.ui.BaseVM
import com.esh1n.core_android.ui.viewmodel.Resource
import com.esh1n.core_android.ui.viewmodel.SingleLiveEvent
import com.lab.esh1n.weather.domain.weather.places.usecase.FetchPlaceForecastUseCase
import com.lab.esh1n.weather.domain.weather.weather.usecases.Args
import com.lab.esh1n.weather.domain.weather.weather.usecases.LoadPlaceAvailableForecastDaysUseCase
import com.lab.esh1n.weather.weather.mapper.AvailableDaysMapper
import com.lab.esh1n.weather.weather.mapper.UiLocalizer
import com.lab.esh1n.weather.weather.model.ForecastDayModel
import javax.inject.Inject

class ForecastWeekVM @Inject constructor(private val loadForecastUseCase: LoadPlaceAvailableForecastDaysUseCase, private val fetchPlaceForecast: FetchPlaceForecastUseCase, uiLocalizer: UiLocalizer) : BaseVM() {

    val availableDays = SingleLiveEvent<Resource<Pair<Int, List<ForecastDayModel>>>>()
    val fetchForecastEvent = SingleLiveEvent<Resource<Unit>>()
    private val availableDaysMapper = AvailableDaysMapper(uiLocalizer)

    fun loadAvailableDays(placeId: Int, selectedDate: Int) {
        loadForecastUseCase
                .perform(Args(placeId, selectedDate))
                .doOnSubscribe { availableDays.postValue(Resource.loading()) }
                .map { availableDays -> Resource.map(availableDays, availableDaysMapper::map) }
                .compose(SchedulersFacade.applySchedulersFlowable())
                .subscribe { models -> availableDays.postValue(models) }
                .disposeOnDestroy()
    }

    fun fetchForecastIfNeeded(placeId: Int) {
        fetchPlaceForecast
                .perform(placeId)
                .compose(SchedulersFacade.applySchedulersSingle())
                .subscribe { res -> fetchForecastEvent.postValue(res) }
                .disposeOnDestroy()

    }
}