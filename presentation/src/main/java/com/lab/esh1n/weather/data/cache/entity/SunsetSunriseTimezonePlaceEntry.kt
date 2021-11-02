package com.lab.esh1n.weather.data.cache.entity

import java.util.*

data class SunsetSunriseTimezonePlaceEntry(
    val id: Int,
    val sunset: Date,
    val sunrise: Date,
    val timezone: String
) {
    companion object {
        fun createEmpty() = SunsetSunriseTimezonePlaceEntry(-1, Date(), Date(), "Europe/Moscow")
    }
}