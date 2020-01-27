package com.lab.esh1n.data.cache.entity

import java.util.*

class SunsetSunrisePlaceEntry(val id: Int, val sunset: Date, val sunrise: Date) {
    companion object {
        fun createEmpty() = SunsetSunrisePlaceEntry(-1, Date(), Date())
    }
}