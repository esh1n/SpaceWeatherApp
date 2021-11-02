package com.lab.esh1n.weather.data.cache.entity

import com.lab.esh1n.weather.data.cache.TemperatureUnit

class Temperature(val value: Double, private val units: TemperatureUnit = TemperatureUnit.C) {

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

    fun middleTemperature(maxTemp: Temperature): Temperature {
        val middleValue = (value + maxTemp.value) / 2
        return Temperature(middleValue)
    }


    companion object {
        fun middleTemperature(minTemp: Double, maxTemp: Double): Temperature {
            val middleValue = (minTemp + maxTemp) / 2
            return Temperature(middleValue)
        }
    }
}



