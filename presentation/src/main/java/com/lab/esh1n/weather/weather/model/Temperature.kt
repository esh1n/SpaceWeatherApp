package com.lab.esh1n.weather.weather.model

import com.lab.esh1n.data.cache.AppPrefs
import java.text.DecimalFormat

class Temperature(val value: Double, val units: TemperatureUnit = TemperatureUnit.C) {

    public fun getHumanReadable(units: TemperatureUnit = TemperatureUnit.C): String {
        val nf = DecimalFormat("##.#")
        return nf.format(value)
    }

    companion object {
        fun middleTemperature(minTemp: Double, maxTemp: Double): Temperature {
            val middleValue = (minTemp + maxTemp) / 2
            return Temperature(middleValue)
        }
    }
}

enum class TemperatureUnit {
    F, C
}

class Wind(val value: Double, val units: AppPrefs.Units = AppPrefs.Units.metric)
