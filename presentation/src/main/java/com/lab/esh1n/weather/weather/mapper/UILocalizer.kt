package com.lab.esh1n.weather.weather.mapper

import com.lab.esh1n.data.cache.IPrefsProvider
import com.lab.esh1n.data.cache.TemperatureUnit
import com.lab.esh1n.data.cache.Units
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.utils.OneValueProperty
import com.lab.esh1n.weather.weather.model.Temperature
import com.lab.esh1n.weather.weather.model.Wind
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class UILocalizerImpl(private val iPrefsProvider: IPrefsProvider) : UiLocalizer {

    private fun getDateFormat(dateFormat: DateFormat, timezone: String): SimpleDateFormat {
        return dateFormat.getSimpleDateFormat(getLocale(), timezone).get()!!
    }

    override fun provideDateMapper(timezone: String, dateFormat: DateFormat): UiDateListMapper {
        return UiDateListMapper(getDateFormat(dateFormat, timezone))
    }

    override fun getLocale(): Locale {
        return iPrefsProvider.getLocale()
    }

    override fun localizeTemperature(temperature: Temperature): OneValueProperty {
        val symbols = DecimalFormatSymbols(getLocale())
        val nf = DecimalFormat("##.#", symbols)
        val convertedValue = temperature.convertTo(iPrefsProvider.getTemperatureUnits())
        val formattedValue = nf.format(convertedValue)
        val stringRes = if (iPrefsProvider.getTemperatureUnits() == TemperatureUnit.C)
            R.string.text_temperature_celsius_str_value
        else
            R.string.text_temperature_fahrenheit_str_value
        return OneValueProperty(stringRes, formattedValue)
    }


    override fun localizeWind(wind: Wind): OneValueProperty {
        val symbols = DecimalFormatSymbols(getLocale())
        val nf = DecimalFormat("##.#", symbols)
        val convertedValue = wind.convertTo(iPrefsProvider.getUnits())
        val formattedValue = nf.format(convertedValue)
        val stringRes = if (wind.units == Units.METRIC)
            R.string.wind_metric
        else
            R.string.wind_imperial
        return OneValueProperty(stringRes, formattedValue)
    }
}


interface UiLocalizer {
    fun localizeTemperature(temperature: Temperature): OneValueProperty
    fun localizeWind(wind: Wind): OneValueProperty
    fun getLocale(): Locale
    fun provideDateMapper(timezone: String, dateFormat: DateFormat): UiDateListMapper
}
