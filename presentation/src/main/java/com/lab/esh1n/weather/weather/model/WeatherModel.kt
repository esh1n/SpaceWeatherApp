package com.lab.esh1n.weather.weather.model

import com.lab.esh1n.weather.utils.OneValueProperty
import com.lab.esh1n.weather.utils.ValueProperty
import java.util.*

sealed class WeatherModel(val humanDate: String,
                          val iconId: String,
                          val tempMax: Int,
                          val tempMin: Int,
                          val description: String,
                          val dayOFTheYear: Int)

class CurrentWeatherModel(
        description: String,
        humanDate: String,
        iconId: String,
        val placeName: String,
        val snow: Int,
        val cloudiness: Int,
        val rain: Int,
        val isDay: Boolean,
        tempMax: Int,
        tempMin: Int,
        val hour24Format: Int,
        val hourWeatherEvents: List<HourWeatherModel>,
        dayOfTheYear: Int) : WeatherModel(humanDate = humanDate, iconId = iconId, tempMax = tempMax, tempMin = tempMin, description = description, dayOFTheYear = dayOfTheYear)

class DayWeatherModel(
    val dayDate: String,
    humanDate: String,
    iconId: String,
    tempMax: Int,
    tempMin: Int,
    description: String,
    dayOfTheYear: Int
) : WeatherModel(
    humanDate = humanDate,
    iconId = iconId,
    tempMax = tempMax,
    tempMin = tempMin,
    description = description,
    dayOFTheYear = dayOfTheYear
)

sealed class HourWeatherModel(val date: Date) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HourWeatherModel) return false

        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        return date.hashCode()
    }
}

class HeaderHourWeatherModel(val isDay: Boolean, val pressure: Int, val windSpeed: OneValueProperty, val humidity: Int, date: Date) : HourWeatherModel(date) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HeaderHourWeatherModel) return false
        if (!super.equals(other)) return false

        if (isDay != other.isDay) return false
        if (pressure != other.pressure) return false
        if (windSpeed != other.windSpeed) return false
        if (humidity != other.humidity) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + isDay.hashCode()
        result = 31 * result + pressure
        result = 31 * result + windSpeed.hashCode()
        result = 31 * result + humidity
        return result
    }
}

class SimpleHourWeatherModel(val isDay: Boolean, val time: String, val iconId: String, val value: ValueProperty, date: Date) : HourWeatherModel(date) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SimpleHourWeatherModel) return false
        if (!super.equals(other)) return false

        if (isDay != other.isDay) return false
        if (time != other.time) return false
        if (iconId != other.iconId) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + isDay.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + iconId.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }
}