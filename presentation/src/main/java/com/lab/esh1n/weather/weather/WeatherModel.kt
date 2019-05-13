package com.lab.esh1n.weather.weather

/**
 * Created by esh1n on 3/16/18.
 */

data class WeatherModel(val id: Long,
                        val cityName: String,
                        val temp: Int,
                        val iconUrl: String,
                        val tempMin: Float,
                        val tempMax: Float,
                        val description: String,
                        val windSpeed: Int,
                        val pressure: Int,
                        val humidity: Int)