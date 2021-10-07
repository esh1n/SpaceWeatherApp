package com.lab.esh1n.data.cache.entity

import androidx.room.Embedded
import java.util.*


class WeatherWithPlace(
        var id: Int,
        var placeName: String,
        var placeId: String,
        var timezone: String,
        var dateSeconds: Long,
        var epochDateMills: Date,
        var dateTxt: String,
        var temperature: Temperature,
        var temperatureMin: Temperature,
        var temperatureMax: Temperature,
        var iconId: String,
        var description: String,
        var windSpeed: WindSpeed,
        var windDegree: WindDegree,
        var pressure: Int,
        var humidity: Int,
        val snow: Int,
        val cloudiness: Int,
        val rain: Int,
        val isCurrent: Boolean,
        val isLiked: Boolean,
        var sunset: Long,
        var sunrise: Long,
        val countryCode: String,
        @Embedded
        val coordinate: Coordinate
)