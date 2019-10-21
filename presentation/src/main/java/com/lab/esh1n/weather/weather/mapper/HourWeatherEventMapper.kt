package com.lab.esh1n.weather.weather.mapper

import com.esh1n.core_android.map.Mapper
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.utils.ValueProperty
import com.lab.esh1n.weather.weather.model.HourWeatherModel
import com.lab.esh1n.weather.weather.model.SimpleHourWeatherModel

class HourWeatherEventMapper(val isDay: Boolean, private val dateMapper: UiDateMapper, private val tempMapper: (Double) -> ValueProperty) : Mapper<WeatherWithPlace, HourWeatherModel>() {

    override fun map(source: WeatherWithPlace): SimpleHourWeatherModel {
        val time = dateMapper.map(source.epochDateMills)
        //TODO fix to load Temperature class from dao
        return SimpleHourWeatherModel(
                isDay,
                time = time,
                iconId = source.iconId,
                value = tempMapper(source.temperature),
                date = source.epochDateMills
        )
    }
}