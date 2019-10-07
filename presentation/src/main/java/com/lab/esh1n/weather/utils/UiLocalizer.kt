package com.lab.esh1n.weather.utils

import com.lab.esh1n.weather.weather.model.Temperature
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

interface UiLocalizer {
    fun localizeTemperature(temperature: Temperature): String
}

class UiLocalizerImpl : UiLocalizer {
    override fun localizeTemperature(temperature: Temperature): String {
        val symbols = DecimalFormatSymbols(Locale.US)
        val nf = DecimalFormat("##.#", symbols)
        return nf.format(temperature.value)
    }

}