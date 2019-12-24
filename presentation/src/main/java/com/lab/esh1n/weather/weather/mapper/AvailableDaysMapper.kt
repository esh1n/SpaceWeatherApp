package com.lab.esh1n.weather.weather.mapper

import com.lab.esh1n.weather.weather.model.ForecastDayModel
import java.util.*

class AvailableDaysMapper(private val uiLocalizer: UiLocalizer) {

    fun map(source: Pair<String, List<Date>>): List<ForecastDayModel> {
        val dateMapper: UiDateMapper = uiLocalizer.provideDateMapper(source.first, DateFormat.DAY_OF_WEEK)
        return source.second.map { it -> ForecastDayModel(dayDescription = dateMapper.map(it), dayDate = it) }
    }

}