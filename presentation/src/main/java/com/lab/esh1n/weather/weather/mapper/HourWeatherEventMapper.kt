package com.lab.esh1n.weather.weather.mapper

import com.esh1n.core_android.map.Mapper
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.weather.model.HourWeather

class HourWeatherEventMapper(private val dateMapper: UiDateMapper) : Mapper<WeatherWithPlace, HourWeather>() {
    override fun map(source: WeatherWithPlace): HourWeather {
        val time = dateMapper.map(source.epochDateMills)
        return HourWeather(
                time = time,
                iconId = source.iconId,
                description = source.temperature.toString()
        )
    }
}