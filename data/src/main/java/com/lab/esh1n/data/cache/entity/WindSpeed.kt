package com.lab.esh1n.data.cache.entity

import com.lab.esh1n.data.cache.Units

class WindSpeed(val value: Double, val units: Units = Units.METRIC) {
    fun convertTo(appUnits: Units): Double {
        return if (this.units == appUnits) {
            value
        } else {
            val isFromMeterToMiles = units == Units.METRIC
            val converter = if (isFromMeterToMiles) this::convertMeterPerSecondsToMilesPerHour else this::convertMilesPerHourToMeterPerSecond
            return converter(value)
        }
    }

    private fun convertMeterPerSecondsToMilesPerHour(value: Double): Double {
        return value * (SECONDS_IN_HOUR / METERS_IN_MILE)
    }

    private fun convertMilesPerHourToMeterPerSecond(value: Double): Double {
        return value * (METERS_IN_MILE / SECONDS_IN_HOUR)
    }

    companion object {
        const val SECONDS_IN_HOUR = 60 * 60
        const val METERS_IN_MILE = 1609.3
    }
}