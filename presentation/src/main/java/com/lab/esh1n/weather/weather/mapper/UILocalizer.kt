package com.lab.esh1n.weather.weather.mapper

import com.lab.esh1n.weather.weather.model.Temperature
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class UILocalizerImpl(private val iLocaleProvider: () -> Locale?) : UiLocalizer {

    private fun getDateFormat(dateFormat: DateFormat): SimpleDateFormat {
        return dateFormat.getSimpleDateFormat(getLocale()).get()!!
    }

    override fun provideDateMapper(timezone: String, dateFormat: DateFormat): UiDateMapper {
        return UiDateMapper(timezone, getDateFormat(dateFormat))
    }

    override fun getLocale(): Locale {
        return iLocaleProvider() ?: Locale.US
    }

    override fun localizeTemperature(temperature: Temperature): String {
        val symbols = DecimalFormatSymbols(getLocale())
        val nf = DecimalFormat("##.#", symbols)
        //TODO convert temperature to appropriate units if needed
        return nf.format(temperature.value)
    }
}


interface UiLocalizer {
    fun localizeTemperature(temperature: Temperature): String
    fun getLocale(): Locale
    fun provideDateMapper(timezone: String, dateFormat: DateFormat): UiDateMapper
}
