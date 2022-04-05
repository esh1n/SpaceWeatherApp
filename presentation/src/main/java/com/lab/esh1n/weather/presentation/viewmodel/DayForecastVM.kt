package com.lab.esh1n.weather.presentation.viewmodel

import com.esh1n.core_android.rx.SchedulersFacade
import com.esh1n.core_android.ui.viewmodel.AutoClearViewModel
import com.esh1n.core_android.ui.viewmodel.Resource
import com.esh1n.core_android.ui.viewmodel.SingleLiveEvent
import com.lab.esh1n.weather.domain.IUILocalisator
import com.lab.esh1n.weather.domain.weather.usecases.LoadDayWeatherUseCase
import com.lab.esh1n.weather.domain.weather.usecases.PlaceDayArgs
import com.lab.esh1n.weather.presentation.adapter.DayForecastSection
import com.lab.esh1n.weather.presentation.adapter.DaytimeForecastModel
import com.lab.esh1n.weather.presentation.mapper.DayForecastSectionsMapper
import javax.inject.Inject


class DayForecastVM @Inject constructor(
    private val loadForecastUseCase: LoadDayWeatherUseCase,
    uiLocalizer: IUILocalisator
) : AutoClearViewModel() {

    val sections =
        SingleLiveEvent<Resource<List<Pair<DayForecastSection, List<DaytimeForecastModel>>>>>()
    private val dayForecastMapper = DayForecastSectionsMapper(uiLocalizer)

    fun loadDayForecastSections(placeAndDay: PlaceDayArgs) {
        loadForecastUseCase
            .perform(placeAndDay)
            .doOnSubscribe { sections.postValue(Resource.loading()) }
            .map { availableDays -> Resource.map(availableDays, dayForecastMapper::map) }
            .compose(SchedulersFacade.applySchedulersSingle())
            .subscribe { models -> sections.postValue(models) }
            .disposeOnDestroy()
    }
}