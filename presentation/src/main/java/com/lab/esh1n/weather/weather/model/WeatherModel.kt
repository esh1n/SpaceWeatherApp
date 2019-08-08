package com.lab.esh1n.weather.weather.model

data class WeatherModel(val id: Int,
                        val cityName: String,
                        val temp: Double,
                        val iconUrl: String,
                        val tempMin: Double,
                        val tempMax: Double,
                        val description: String,
                        val windSpeed: Float,
                        val windDegree: Int,
                        val pressure: Int,
                        val humidity: Int,
                        val dateStr: String,
                        val dateTxt: String)