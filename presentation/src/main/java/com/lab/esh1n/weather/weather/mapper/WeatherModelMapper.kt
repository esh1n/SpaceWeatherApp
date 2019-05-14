package com.lab.esh1n.weather.weather.mapper

import com.lab.esh1n.data.cache.entity.WeatherEntity
import com.lab.esh1n.weather.domain.base.Mapper
import com.lab.esh1n.weather.weather.WeatherModel


class WeatherModelMapper : Mapper<WeatherEntity, WeatherModel>() {

    private val dateMapper = UiDateMapper()
    override fun map(source: WeatherEntity): WeatherModel {
        return WeatherModel(
                id = source.id,
                cityName = source.cityName,
                temp = source.temperature,
                tempMax = source.temperatureMax,
                tempMin = source.temperatureMin,
                iconUrl = prepareIconUrl(source.iconId),
                description = source.description,
                windSpeed = source.windSpeed,
                windDegree = source.windDegree,
                pressure = source.pressure,
                humidity = source.humidity,
                dateStr = dateMapper.map(source.date)
        )
    }

    private fun prepareIconUrl(iconCode: String): String {
        return "$OPENMAP_URL/img/w/$iconCode.png"
    }

    companion object {
        const val OPENMAP_URL = "https://openweathermap.org"
    }
}