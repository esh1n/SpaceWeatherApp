package com.lab.esh1n.weather.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.esh1n.core_android.rx.SchedulersFacade
import com.esh1n.core_android.ui.viewmodel.AutoClearViewModel
import com.esh1n.core_android.ui.viewmodel.Resource
import com.esh1n.core_android.ui.viewmodel.SingleLiveEvent
import com.lab.esh1n.weather.domain.IUILocalisator
import com.lab.esh1n.weather.domain.places.PlacesRepository
import com.lab.esh1n.weather.domain.places.usecase.FetchPlaceForecastUseCase
import com.lab.esh1n.weather.domain.weather.usecases.Args
import com.lab.esh1n.weather.domain.weather.usecases.LoadPlaceAvailableForecastDaysUseCase
import com.lab.esh1n.weather.presentation.mapper.AvailableDaysMapper
import com.lab.esh1n.weather.presentation.model.ForecastDayModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class ForecastWeekVM @Inject constructor(
    private val loadForecastUseCase: LoadPlaceAvailableForecastDaysUseCase,
    private val fetchPlaceForecast: FetchPlaceForecastUseCase,
    private val placesRepository: PlacesRepository,
    uiLocalizer: IUILocalisator
) : AutoClearViewModel() {

    val availableDays = SingleLiveEvent<Resource<Pair<Int, List<ForecastDayModel>>>>()
    val fetchForecastEvent = SingleLiveEvent<Resource<Unit>>()
    private val availableDaysMapper = AvailableDaysMapper(uiLocalizer)


    fun getFavouriteStateFlow(placeId: Int) = placesRepository.getIsPlaceFavourite(placeId)

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

    fun changeFavouriteState(placeId: Int, checked: Boolean) {
        viewModelScope.launch {
            placesRepository.changeFavouriteState(placeId, checked)
        }
    }
}