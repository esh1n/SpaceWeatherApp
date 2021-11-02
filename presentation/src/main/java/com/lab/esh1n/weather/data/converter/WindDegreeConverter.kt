package com.lab.esh1n.weather.data.converter

import androidx.room.TypeConverter
import com.lab.esh1n.weather.data.cache.entity.WindDegree
import com.lab.esh1n.weather.data.cache.entity.WindDirection
import kotlin.math.roundToInt

class WindDegreeConverter {
    @TypeConverter
    fun toWindDegree(input: Double?): WindDegree {
        return mapToWindDegree(input)
    }

    @TypeConverter
    fun toRawValue(windDegree: WindDegree?): Double {
        return windDegree?.degree ?: 0.0
    }

    companion object {
        fun mapToWindDegree(input: Double?): WindDegree {
            val degree = input ?: 0.0
            return WindDegree(degree, initWindDirection(degree))
        }

        private fun initWindDirection(degree: Double): WindDirection {
            val directions = WindDirection.values()
            val position: Int = ((degree % 360) / 45).roundToInt()
            return directions[position % 8]
        }
    }


}