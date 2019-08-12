package com.lab.esh1n.weather.weather.model

sealed class WeatherModel(val humanDate: String,
                          val iconId: String,
                          val tempMin: Double,
                          val tempMax: Double)

class CurrentWeatherModel(
        val description: String,
        humanDate: String,
        iconId: String,
        tempMin: Double,
        tempMax: Double) : WeatherModel(humanDate, iconId, tempMin, tempMax)

class DayWeatherModel(val dayDate: String,
                      humanDate: String,
                      iconId: String,
                      tempMin: Double,
                      tempMax: Double) : WeatherModel(humanDate, iconId, tempMin, tempMax)