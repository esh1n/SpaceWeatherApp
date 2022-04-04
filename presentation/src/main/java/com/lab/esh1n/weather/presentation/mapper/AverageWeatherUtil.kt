package com.lab.esh1n.weather.presentation.mapper

import com.lab.esh1n.weather.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.data.cache.entity.WindDegree
import com.lab.esh1n.weather.data.cache.entity.WindDirection
import java.text.DecimalFormat
import kotlin.math.roundToInt

object AverageWeatherUtil {
    fun calculateWeatherIconAndDescription(weathers: List<WeatherWithPlace>): Pair<String, String> {
        if (weathers.isEmpty()) {
            return Pair("00d", "n/a")
        }
        val iconsIds: List<Int> = weathers.map { Integer.parseInt(it.iconId.substring(0, 2)) }
        val mostOccasionsIcon =
            iconsIds.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key ?: 1
        val iconDay = getIconCode(mostOccasionsIcon, true)
        val weatherItem = weathers.find { it.iconId == iconDay }
        return if (weatherItem != null) {
            Pair(iconDay, weatherItem.description)
        } else {
            val iconNight = getIconCode(mostOccasionsIcon, false)
            val description = weathers.find { it.iconId == iconNight }?.description ?: "n/a"
            Pair(iconNight, description)
        }
    }

    fun averageWindDirection(degrees: List<WindDegree>): WindDirection {
        val directions = degrees.map { it.direction }
        return directions.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key
            ?: WindDirection.N_A
    }

    fun averageInt(items: List<Double>): Int {
        val average: Double = items.average()
        return if (average.isNaN()) 0 else average.roundToInt()
    }

    fun average(items: List<Double>): Double {
        val average: Double = items.average()
        return if (average.isNaN()) 0.0 else average
    }

    private fun getIconCode(value: Int, isDay: Boolean): String {
        val formatter = DecimalFormat("00")
        val dayOrNightChar = if (isDay) 'd' else 'n'
        return "${formatter.format(value)}$dayOrNightChar"
    }
}