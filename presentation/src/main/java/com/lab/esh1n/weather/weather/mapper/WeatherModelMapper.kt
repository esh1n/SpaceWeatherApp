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
            val dateMapper = UiDateMapper(timezone, UILocalizer.getDateFormat(DateFormat.MONTH_DAY))
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
                    iconId = value.iconId,
                    snow = value.))
            val dayWeathers = dayToForecast.mapValues { (_, values) ->
                val first = values[0]
                val weathersToAnalyse = values
                        .filter {
                            val hourOfDay = DateBuilder(it.measured_at, it.timezone).getHour24Format()
                            hourOfDay in 9..21
                        }
                val averageTempMax = weathersToAnalyse.map { it.temperatureMax }.average()
                val averageTempMin = weathersToAnalyse.map { it.temperatureMin }.average()
                val averageIcon = calculateWeatherIcon(weathersToAnalyse)
                val dateOfWeekMapper = UiDateMapper(timezone, UILocalizer.getDateFormat(DateFormat.DAY_OF_WEEK))
                val dayOfWeek = dateOfWeekMapper.map(first.measured_at)
                DayWeatherModel(dayDate = dateMapper.map(first.measured_at),
                        humanDate = dayOfWeek,
                        tempMax = averageTempMax.toInt(),
                        tempMin = averageTempMin.toInt(),
                        iconId = averageIcon)
            }.values.drop(1)
            resultWeatherModel.addAll(dayWeathers)
            return resultWeatherModel

        }

    }

    private fun calculateWeatherIcon(weathers: List<WeatherWithPlace>): String {
        if (weathers.isEmpty()) {
            return "01d";
        }
        //TODO play with different locations and weather data to calculate summary icon
        return weathers[(weathers.size / 2)].iconId
    }


}