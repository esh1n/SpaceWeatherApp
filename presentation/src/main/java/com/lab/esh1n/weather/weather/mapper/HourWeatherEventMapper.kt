package com.lab.esh1n.weather.weather.mapper

import com.esh1n.core_android.map.Mapper
import com.esh1n.utils_android.DateBuilder
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.utils.ValueProperty
import com.lab.esh1n.weather.weather.model.HourWeatherModel
import com.lab.esh1n.weather.weather.model.SimpleHourWeatherModel
import java.util.*

class HourWeatherEventMapper(val isDay: Boolean, private val dateHourMapper: UiDateMapper, private val dateHourDayMapper: UiDateMapper, private val tempMapper: (Double) -> ValueProperty) : Mapper<WeatherWithPlace, HourWeatherModel>() {

    override fun map(source: WeatherWithPlace): SimpleHourWeatherModel {
        val isToday = DateBuilder(source.epochDateMills).isSameDay(Date())
        val dateMapper = if (isToday) dateHourMapper else dateHourDayMapper
        //TODO fix to load Temperature class from dao
        return SimpleHourWeatherModel(
                isDay,
                time = dateMapper.map(source.epochDateMills),
                iconId = source.iconId,
                value = tempMapper(source.temperature),
                date = source.epochDateMills
        )
    }
}