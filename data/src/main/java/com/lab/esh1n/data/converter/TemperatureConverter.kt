package com.lab.esh1n.data.converter

import androidx.room.TypeConverter
import com.lab.esh1n.data.cache.TemperatureUnit
import com.lab.esh1n.data.cache.entity.Temperature

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