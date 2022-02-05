package com.lab.esh1n.weather.data.converter

import androidx.room.TypeConverter
import com.lab.esh1n.weather.data.cache.entity.Temperature
import com.lab.esh1n.weather.domain.TemperatureUnit

class TemperatureConverter {
    @TypeConverter
    fun toRawValue(temperature: Temperature?): Double {
        return temperature?.value ?: 0.0
    }

    @TypeConverter
    fun toTemperature(tempValue: Double?): Temperature {
        return Temperature(tempValue ?: 0.0, TemperatureUnit.C)
    }

}