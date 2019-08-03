package com.lab.esh1n.data.cache.entity

import java.util.*


class WeatherWithPlace(

        var id: Int,

        var placeName: String,

        var placeId: String,

        var measured_at: Date,

        var temperature: Double,

        var temperatureMin: Double,

        var temperatureMax: Double,

        var iconId: String,

        var description: String,

        var windSpeed: Float,

        var windDegree: Int,

        var pressure: Int,

        var humidity: Int) {

}