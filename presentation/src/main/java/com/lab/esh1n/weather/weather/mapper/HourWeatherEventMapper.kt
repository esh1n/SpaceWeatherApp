package com.lab.esh1n.weather.weather.mapper

import com.esh1n.core_android.map.Mapper
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.weather.model.HourWeatherModel
import com.lab.esh1n.weather.weather.model.SimpleHourWeatherModel

class HourWeatherEventMapper(val isDay: Boolean, private val dateMapper: UiDateMapper, private val tempMapper: (Double) -> String) : Mapper<WeatherWithPlace, HourWeatherModel>() {

    override fun map(source: WeatherWithPlace): SimpleHourWeatherModel {
        val time = dateMapper.map(source.epochDateMills)
        //TODO fix to load Temperature class from dao
        return SimpleHourWeatherModel(
                isDay,
                time = time,
                iconId = source.iconId,
                description = tempMapper(source.temperature)
        )
    }
}