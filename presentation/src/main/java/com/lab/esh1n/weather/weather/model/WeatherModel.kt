package com.lab.esh1n.weather.weather.model

import com.lab.esh1n.weather.utils.ValueProperty
import java.util.*

sealed class WeatherModel(val humanDate: String,
                          val iconId: String,
                          val tempMax: Int,
                          val tempMin: Int,
                          val description: String
)

class CurrentWeatherModel(
        description: String,
        humanDate: String,
        iconId: String,
        val placeName: String,
        val snow: Int,
        val cloudiness: Int,
        val rain: Int,
        val isDay: Boolean,
        tempMax: Int,
        tempMin: Int,
        val hour24Format: Int, val hourWeatherEvents: List<HourWeatherModel>) : WeatherModel(humanDate = humanDate, iconId = iconId, tempMax = tempMax, tempMin = tempMin, description = description)

class DayWeatherModel(val dayDate: String,
                      humanDate: String,
                      iconId: String,
                      tempMax: Int,
                      tempMin: Int,
                      description: String) : WeatherModel(humanDate = humanDate, iconId = iconId, tempMax = tempMax, tempMin = tempMin, description = description)

sealed class HourWeatherModel(val date: Date)
class HeaderHourWeatherModel(val isDay: Boolean, val pressure: Int, val wind: Int, val humidity: Int, date: Date) : HourWeatherModel(date)
class SimpleHourWeatherModel(val isDay: Boolean, val time: String, val iconId: String, val value: ValueProperty, date: Date) : HourWeatherModel(date)