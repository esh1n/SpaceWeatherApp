package com.lab.esh1n.weather.weather.viewmodel

import com.esh1n.core_android.rx.SchedulersFacade
import com.esh1n.core_android.ui.BaseVM
import com.esh1n.core_android.ui.viewmodel.Resource
import com.esh1n.core_android.ui.viewmodel.SingleLiveEvent
import com.lab.esh1n.weather.domain.weather.usecases.LoadDayWeatherUseCase
import com.lab.esh1n.weather.domain.weather.usecases.PlaceDayArgs
import com.lab.esh1n.weather.weather.adapter.DayForecastSection
import com.lab.esh1n.weather.weather.adapter.DaytimeForecastModel
import com.lab.esh1n.weather.weather.mapper.DayForecastSectionsMapper
import com.lab.esh1n.weather.weather.mapper.UiLocalizer
import javax.inject.Inject


class DayForecastVM @Inject constructor(private val loadForecastUseCase: LoadDayWeatherUseCase, uiLocalizer: UiLocalizer) : BaseVM() {

    val sections = SingleLiveEvent<Resource<List<Pair<DayForecastSection, List<DaytimeForecastModel>>>>>()
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