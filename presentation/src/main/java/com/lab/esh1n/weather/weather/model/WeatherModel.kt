package com.lab.esh1n.weather.weather.model

sealed class WeatherModel(val humanDate: String,
                          val iconId: String,
                          val tempMin: Int,
                          val tempMax: Int)

class CurrentWeatherModel(
        val description: String,
        humanDate: String,
        iconId: String,
        currentTemperature: Int,
        tempMin: Int,
        tempMax: Int) : WeatherModel(humanDate, iconId, tempMin, tempMax)

class DayWeatherModel(val dayDate: String,
                      humanDate: String,
                      iconId: String,
                      tempMin: Int,
                      tempMax: Int) : WeatherModel(humanDate, iconId, tempMin, tempMax)