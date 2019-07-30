package com.lab.esh1n.weather.weather.model

data class WeatherModel(val id: Long,
                        val cityName: String,
                        val temperature: Int,
                        val iconUrl: String,
                        val tempMin: Float,
                        val tempMax: Float,
                        val description: String,
                        val windSpeed: Int,
                        val pressure: Int,
                        val humidity: Int)