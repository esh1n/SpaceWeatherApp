package com.lab.esh1n.weather.weather.model

import com.lab.esh1n.data.cache.TemperatureUnit
import com.lab.esh1n.data.cache.Units

class Temperature(val value: Double, val units: TemperatureUnit = TemperatureUnit.C) {

    fun convertTo(temperatureUnits: TemperatureUnit): Double {
        return if (units == temperatureUnits) {
            value
        } else {
            val isCelsiusToFahrenheit = units == TemperatureUnit.C
            val converter = if (isCelsiusToFahrenheit) this::convertCelsiusToFahrenheit else this::convertFahrenheitToCelsius
            converter(value)
        }

    }

    private fun convertCelsiusToFahrenheit(celsiusValue: Double): Double {
        return (9.0 / 5.0) * celsiusValue + 32
    }

    private fun convertFahrenheitToCelsius(fahrenheitValue: Double): Double {
        return ((fahrenheitValue - 32) * 5) / 9
    }

    companion object {
        fun middleTemperature(minTemp: Double, maxTemp: Double): Temperature {
            val middleValue = (minTemp + maxTemp) / 2
            return Temperature(middleValue)
        }
    }
}


class Wind(val value: Double, val units: Units = Units.METRIC) {
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
