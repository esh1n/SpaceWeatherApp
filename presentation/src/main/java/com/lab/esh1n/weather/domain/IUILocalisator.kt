package com.lab.esh1n.weather.domain

import com.lab.esh1n.weather.data.cache.entity.Temperature
import com.lab.esh1n.weather.data.cache.entity.WindDirection
import com.lab.esh1n.weather.data.cache.entity.WindSpeed
import com.lab.esh1n.weather.utils.OneValueProperty
import com.lab.esh1n.weather.utils.StringResValueProperty
import com.lab.esh1n.weather.weather.mapper.DateFormat
import com.lab.esh1n.weather.weather.mapper.UiDateListMapper

interface IUILocalisator {
    fun localizeTemperature(temperature: Temperature): OneValueProperty
    fun localizeWindSpeed(windSpeed: WindSpeed): OneValueProperty
    fun localizeWindDirection(windDirection: WindDirection): StringResValueProperty
    fun provideDateMapper(timezone: String, dateFormat: DateFormat): UiDateListMapper
}