package com.lab.esh1n.data.cache.entity

import java.util.*


class WeatherWithPlace(

        var id: Int,

        var placeName: String,

        var placeId: String,

        var timezone: String,

        var measured_at: Date,

        var dateTxt: String,

        var temperature: Double,

        var temperatureMin: Double,

        var temperatureMax: Double,

        var iconId: String,

        var description: String,

        var windSpeed: Float,

        var windDegree: Int,

        var pressure: Int,

        var humidity: Int,
        val snow: Int,
        val cloudiness: Int,
        val rain: Int,
        val isCurrent: Boolean)