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
        currentTemperature: Int,
        val placeName: String,
        val snow: Int,
        val cloudiness: Int,
        val rain: Int,
        val isDay: Boolean,
        tempMin: Int,
        tempMax: Int,
        val hour24Format: Int) : WeatherModel(humanDate, iconId, tempMin, tempMax)

class DayWeatherModel(val dayDate: String,
                      humanDate: String,
                      iconId: String,
                      tempMin: Int,
                      tempMax: Int) : WeatherModel(humanDate, iconId, tempMin, tempMax)