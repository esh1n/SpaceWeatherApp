package com.lab.esh1n.weather.weather.mapper

import com.esh1n.core_android.map.Mapper
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.weather.WeatherModel


class WeatherModelMapper : Mapper<WeatherWithPlace, WeatherModel>() {


    override fun map(source: WeatherWithPlace): WeatherModel {
        val dateMapper = UiDateMapper(source.timezone)
        return WeatherModel(
                id = source.id,
                cityName = source.placeName,
                temp = source.temperature,
                tempMax = source.temperatureMax,
                tempMin = source.temperatureMin,
                iconUrl = prepareIconUrl(source.iconId),
                description = source.description,
                windSpeed = source.windSpeed,
                windDegree = source.windDegree,
                pressure = source.pressure,
                humidity = source.humidity,
                dateStr = dateMapper.map(source.measured_at),
                dateTxt = source.dateTxt
        )
    }

    private fun prepareIconUrl(iconCode: String): String {
        return "$OPENMAP_URL/img/w/$iconCode.png"
    }

    companion object {
        const val OPENMAP_URL = "https://openweathermap.org"
    }
}