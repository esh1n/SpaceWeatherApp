package com.lab.esh1n.weather.weather.mapper

import com.lab.esh1n.data.cache.AppPrefs
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.utils.OneValueProperty
import com.lab.esh1n.weather.weather.model.Temperature
import com.lab.esh1n.weather.weather.model.TemperatureUnit
import com.lab.esh1n.weather.weather.model.Wind
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class UILocalizerImpl(private val iLocaleProvider: () -> Locale?) : UiLocalizer {
    //TODO also provide UNITS - wind and temperature - besides Locale, make appprefs implement interface of locale and Units providing
    //TODO and let's get rid of static units in temperature and wind

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
        val stringRes = if (temperature.units == TemperatureUnit.C)
            R.string.text_temperature_celsius_str_value
        else
            R.string.text_temperature_fahrenheit_str_value
        return OneValueProperty(stringRes, value)
    }

    override fun localizeWind(wind: Wind): OneValueProperty {
        val symbols = DecimalFormatSymbols(getLocale())
        val nf = DecimalFormat("##.#", symbols)
        //TODO convert wind to appropriate units if needed
        val value = nf.format(wind.value)
        val stringRes = if (wind.units == AppPrefs.Units.metric)
            R.string.wind_metric
        else
            R.string.wind_imperial
        return OneValueProperty(stringRes, value)
    }
}


interface UiLocalizer {
    fun localizeTemperature(temperature: Temperature): OneValueProperty
    fun localizeWind(wind: Wind): OneValueProperty
    fun getLocale(): Locale
    fun provideDateMapper(timezone: String, dateFormat: DateFormat): UiDateMapper
}
