package com.lab.esh1n.weather.weather.model

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

sealed class HourWeatherModel(val time: String)
class HeaderHourWeatherModel(val isDay: Boolean, time: String, val pressure: Int, val wind: Int, val humidity: Int) : HourWeatherModel(time)
class SimpleHourWeatherModel(val isDay: Boolean, time: String, val iconId: String, val description: String) : HourWeatherModel(time)