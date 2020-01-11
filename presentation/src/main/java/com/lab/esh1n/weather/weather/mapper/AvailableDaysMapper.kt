package com.lab.esh1n.weather.weather.mapper

import com.lab.esh1n.weather.domain.weather.weather.usecases.AvailableDaysResult
import com.lab.esh1n.weather.weather.model.ForecastDayModel

class AvailableDaysMapper(private val uiLocalizer: UiLocalizer) {

    fun map(source: AvailableDaysResult): Pair<Int, List<ForecastDayModel>> {
        val placeId = source.placeId
        val timezone = source.timezone
        val dates = source.dates
        val dateMapper: UiDateListMapper = uiLocalizer.provideDateMapper(timezone, DateFormat.DAY_OF_WEEK_SHORT)
        val forecastModels = dates.map { ForecastDayModel(dayDescription = dateMapper.map(it), dayDate = it, placeId = placeId, timezone = timezone) }
        return Pair(source.selectedDateIndex, forecastModels)
    }

}