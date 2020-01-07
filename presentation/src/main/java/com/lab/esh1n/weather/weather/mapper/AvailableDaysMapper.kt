package com.lab.esh1n.weather.weather.mapper

import com.lab.esh1n.weather.weather.model.ForecastDayModel
import java.util.*

class AvailableDaysMapper(private val uiLocalizer: UiLocalizer) {

    fun map(source: Triple<Int, String, List<Date>>): List<ForecastDayModel> {
        val placeId = source.first
        val timezone = source.second
        val dates = source.third
        val dateMapper: UiDateListMapper = uiLocalizer.provideDateMapper(timezone, DateFormat.DAY_OF_WEEK_SHORT)
        return dates.map { ForecastDayModel(dayDescription = dateMapper.map(it), dayDate = it, placeId = placeId, timezone = timezone) }
    }

}