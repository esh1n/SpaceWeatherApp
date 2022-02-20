package com.lab.esh1n.weather.data.converter

import androidx.room.TypeConverter
import com.lab.esh1n.weather.data.cache.entity.WindSpeed
import com.lab.esh1n.weather.domain.prefs.Units

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