package com.lab.esh1n.weather.weather.mapper

import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.data.cache.entity.Temperature
import com.lab.esh1n.weather.data.cache.entity.WindDirection
import com.lab.esh1n.weather.data.cache.entity.WindSpeed
import com.lab.esh1n.weather.domain.IPrefsInteractor
import com.lab.esh1n.weather.domain.IUILocalisator
import com.lab.esh1n.weather.domain.TemperatureUnit
import com.lab.esh1n.weather.domain.Units
import com.lab.esh1n.weather.utils.OneValueProperty
import com.lab.esh1n.weather.utils.StringResValueProperty
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat

class UILocalizerImpl(private val prefsInteractor: IPrefsInteractor) : IUILocalisator {

    override fun localizeWindDirection(windDirection: WindDirection): StringResValueProperty {
        val stringsRes = when (windDirection) {
            WindDirection.EAST -> R.string.text_direction_EAST
            WindDirection.NORTH -> R.string.text_direction_NORTH
            WindDirection.NORTH_EAST -> R.string.text_direction_NORTH_EAST
            WindDirection.SOUTH_EAST -> R.string.text_direction_SOUTH_EAST
            WindDirection.SOUTH -> R.string.text_direction_SOUTH
            WindDirection.SOUTH_WEST -> R.string.text_direction_SOUTH_WEST
            WindDirection.WEST -> R.string.text_direction_WEST
            WindDirection.NORTH_WEST -> R.string.text_direction_SOUTH_NORTH_WEST
            else -> R.string.text_not_defined
        }
        return StringResValueProperty(stringsRes)
    }

    private fun getDateFormat(dateFormat: DateFormat, timezone: String): SimpleDateFormat {
        return dateFormat.getSimpleDateFormat(prefsInteractor.getLocaleBlocking(), timezone).get()!!
    }

    override fun provideDateMapper(timezone: String, dateFormat: DateFormat): UiDateListMapper {
        return UiDateListMapper(getDateFormat(dateFormat, timezone))
    }

    override fun localizeTemperature(temperature: Temperature): OneValueProperty {
        val symbols = DecimalFormatSymbols(prefsInteractor.getLocaleBlocking())
        val nf = DecimalFormat("##.#", symbols)
        val convertedValue = temperature.convertTo(prefsInteractor.getAppTemperatureUnits())
        val formattedValue = nf.format(convertedValue)
        val stringRes = if (prefsInteractor.getAppTemperatureUnits() == TemperatureUnit.C)
            R.string.text_temperature_celsius_str_value
        else
            R.string.text_temperature_fahrenheit_str_value
        return OneValueProperty(stringRes, formattedValue)
    }


    override fun localizeWindSpeed(windSpeed: WindSpeed): OneValueProperty {
        val symbols = DecimalFormatSymbols(prefsInteractor.getLocaleBlocking())
        val nf = DecimalFormat("##.#", symbols)
        val convertedValue = windSpeed.convertTo(prefsInteractor.getMeasureUnits())
        val formattedValue = nf.format(convertedValue)
        val stringRes = if (windSpeed.units == Units.METRIC)
            R.string.wind_metric
        else
            R.string.wind_imperial
        return OneValueProperty(stringRes, formattedValue)
    }
}


