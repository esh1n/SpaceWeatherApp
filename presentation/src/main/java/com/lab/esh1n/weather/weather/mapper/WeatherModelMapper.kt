package com.lab.esh1n.weather.weather.mapper

import com.esh1n.utils_android.DateBuilder
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.weather.model.CurrentWeatherModel
import com.lab.esh1n.weather.weather.model.DayWeatherModel
import com.lab.esh1n.weather.weather.model.Temperature
import com.lab.esh1n.weather.weather.model.WeatherModel
import java.util.*


class WeatherModelMapper {


    fun map(source: List<WeatherWithPlace>): List<WeatherModel> {
        if (source.isEmpty()) {
            return emptyList()
        } else {
            val timezone = source[0].timezone
            val dateMapper = UiDateMapper(timezone)
            val dayToForecast: TreeMap<Int, MutableList<WeatherWithPlace>> = TreeMap()
            source.forEach { weather ->
                val day = DateBuilder(weather.measured_at).getDay()
                var list = dayToForecast[day]
                if (list == null) {
                    list = mutableListOf(weather).apply {
                        add(weather)
                    }
                    dayToForecast[day] = list
                } else {
                    list.add(weather)
                }
            }
            val value = dayToForecast.firstEntry().value[0]
            val resultWeatherModel = mutableListOf<WeatherModel>()
            resultWeatherModel.add(CurrentWeatherModel(
                    description = value.description,
                    humanDate = dateMapper.map(value.measured_at),
                    tempMax = value.temperatureMax.toInt(),
                    tempMin = value.temperatureMin.toInt(),
                    currentTemperature = Temperature.middleTemperature(value.temperatureMin, value.temperatureMax).value.toInt(),
                    iconId = value.iconId))
            val dayWeathers = dayToForecast.mapValues { (_, values) ->
                val first = values[0]
                val averageTempMax = values.map { it.temperatureMax }.average()
                val averageTempMin = values.map { it.temperatureMin }.average()
                val averageIcon = values.map { it.iconId.substring(0, 1).toInt() }.average().toInt()
                val averageIconStr = String.format("%02d", averageIcon) + "d"
                DayWeatherModel(dayDate = dateMapper.map(first.measured_at),
                        humanDate = "N/A",
                        tempMax = averageTempMax.toInt(),
                        tempMin = averageTempMin.toInt(),
                        iconId = averageIconStr)
            }.values.drop(1)
            resultWeatherModel.addAll(dayWeathers)
            return resultWeatherModel

        }

    }


}