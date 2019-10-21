package com.lab.esh1n.weather.weather.model

import com.lab.esh1n.weather.utils.ValueProperty
import java.util.*

sealed class WeatherModel(val humanDate: String,
                          val iconId: String,
                          val tempMin: Int,
                          val tempMax: Int
)

class CurrentWeatherModel(
        val description: String,
        humanDate: String,
        iconId: String,
        currentTemperature: String,
        val placeName: String,
        val snow: Int,
        val cloudiness: Int,
        val rain: Int,
        val isDay: Boolean,
        tempMin: Int,
        tempMax: Int,
        val hour24Format: Int, val hourWeatherEvents: List<HourWeatherModel>) : WeatherModel(humanDate, iconId, tempMin, tempMax)

class DayWeatherModel(val dayDate: String,
                      humanDate: String,
                      iconId: String,
                      tempMin: Int,
                      tempMax: Int) : WeatherModel(humanDate, iconId, tempMin, tempMax)

sealed class HourWeatherModel(val date: Date)
class HeaderHourWeatherModel(val isDay: Boolean, val pressure: Int, val wind: Int, val humidity: Int, date: Date) : HourWeatherModel(date)
class SimpleHourWeatherModel(val isDay: Boolean, val time: String, val iconId: String, val value: ValueProperty, date: Date) : HourWeatherModel(date)