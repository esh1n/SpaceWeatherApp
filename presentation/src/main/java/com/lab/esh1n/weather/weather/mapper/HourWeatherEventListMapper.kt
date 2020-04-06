package com.lab.esh1n.weather.weather.mapper

import com.esh1n.core_android.map.ListMapper
import com.esh1n.utils_android.DateBuilder
import com.lab.esh1n.data.cache.entity.Temperature
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.utils.ValueProperty
import com.lab.esh1n.weather.weather.model.HourWeatherModel
import com.lab.esh1n.weather.weather.model.SimpleHourWeatherModel
import java.util.*

class HourWeatherEventListMapper(val isDay: Boolean, private val dateHourMapper: UiDateListMapper, private val dateHourDayMapper: UiDateListMapper, private val tempMapper: (Temperature) -> ValueProperty) : ListMapper<WeatherWithPlace, HourWeatherModel>() {

    override fun map(source: WeatherWithPlace): SimpleHourWeatherModel {
        val isToday = DateBuilder(source.epochDateMills).isSameDay(Date())
        val dateMapper = if (isToday) dateHourMapper else dateHourDayMapper
        return SimpleHourWeatherModel(
                isDay,
                time = dateMapper.map(source.epochDateMills),
                iconId = source.iconId,
                value = tempMapper(source.temperature),
                date = source.epochDateMills
        )
    }
}