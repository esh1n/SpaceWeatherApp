package com.lab.esh1n.data.converter

import androidx.room.TypeConverter
import com.lab.esh1n.data.cache.Units
import com.lab.esh1n.data.cache.entity.WindSpeed

class WindSpeedConverter {

    @TypeConverter
    fun toRawValue(windSpeed: WindSpeed?): Double {
        return windSpeed?.convertTo(Units.METRIC) ?: 0.0
    }

    @TypeConverter
    fun toWindSpeed(rawValue: Double?): WindSpeed {
        return WindSpeed(rawValue ?: 0.0, Units.METRIC)
    }
}