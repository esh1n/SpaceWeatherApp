package com.lab.esh1n.weather.weather.mapper

import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.utils.OneValueProperty
import com.lab.esh1n.weather.weather.model.Temperature
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class UILocalizerImpl(private val iLocaleProvider: () -> Locale?) : UiLocalizer {

    private fun getDateFormat(dateFormat: DateFormat, timezone: String): SimpleDateFormat {
        return dateFormat.getSimpleDateFormat(getLocale(), timezone).get()!!
    }

    override fun provideDateMapper(timezone: String, dateFormat: DateFormat): UiDateMapper {
        return UiDateMapper(getDateFormat(dateFormat, timezone))
    }

    override fun getLocale(): Locale {
        return iLocaleProvider() ?: Locale.US
    }

    override fun localizeTemperature(temperature: Temperature): OneValueProperty {
        val symbols = DecimalFormatSymbols(getLocale())
        val nf = DecimalFormat("##.#", symbols)
        //TODO convert temperature to appropriate units if needed
        val value = nf.format(temperature.value)
        return OneValueProperty(R.string.text_temperature_celsius_str_value, value)
    }
}



interface UiLocalizer {
    fun localizeTemperature(temperature: Temperature): OneValueProperty
    fun getLocale(): Locale
    fun provideDateMapper(timezone: String, dateFormat: DateFormat): UiDateMapper
}
