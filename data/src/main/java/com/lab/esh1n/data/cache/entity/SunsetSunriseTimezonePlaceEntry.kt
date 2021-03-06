package com.lab.esh1n.data.cache.entity

import java.util.*

class SunsetSunriseTimezonePlaceEntry(val id: Int, val sunset: Date, val sunrise: Date, val timezone: String) {
    companion object {
        fun createEmpty() = SunsetSunriseTimezonePlaceEntry(-1, Date(), Date(), "Europe/Moscow")
    }
}