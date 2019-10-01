package com.lab.esh1n.weather.weather.mapper

import android.util.Log
import com.esh1n.utils_android.DateBuilder
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.weather.model.CurrentWeatherModel
import com.lab.esh1n.weather.weather.model.DayWeatherModel
import com.lab.esh1n.weather.weather.model.Temperature
import com.lab.esh1n.weather.weather.model.WeatherModel
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.abs


class WeatherModelMapper {


    fun map(source: List<WeatherWithPlace>): List<WeatherModel> {
        if (source.isEmpty()) {
            return emptyList()
        } else {
            val firstWeather = source[0]
            val timezone = firstWeather.timezone
            val firstDay = DateBuilder(firstWeather.epochDateMills).getDay()
            val dateMapper = UiDateMapper(timezone, UILocalizer.getDateFormat(DateFormat.MONTH_DAY))
            val dayToForecast = createDayMap(source)
            val firstDayForecasts = dayToForecast[firstDay]
            dayToForecast.remove(firstDay)
            val currentWeatherModel = mapCurrentWeatherModel(firstDayForecasts
                    ?: arrayListOf(), timezone, dateMapper)
            Log.d("CurrentP", "!!!END---------------------------------------")
            val resultWeatherModel = mutableListOf<WeatherModel>()
            resultWeatherModel.add(currentWeatherModel)
            resultWeatherModel.addAll(mapOtherDay(dayToForecast, timezone, dateMapper))
            return resultWeatherModel
        }
    }

    private fun mapOtherDay(dayToForecast: HashMap<Int, MutableList<WeatherWithPlace>>, timezone: String, dateMapper: UiDateMapper): Collection<DayWeatherModel> {
        return dayToForecast.mapValues { (_, values) ->
            val first = values[0]
            val weathersToAnalyse = values
                    .filter {
                        val hourOfDay = DateBuilder(it.epochDateMills, it.timezone).getHour24Format()
                        hourOfDay in 9..21
                    }
            val averageTempMax = weathersToAnalyse.map { it.temperatureMax }.average()
            val averageTempMin = weathersToAnalyse.map { it.temperatureMin }.average()
            val averageIcon = calculateWeatherIcon(weathersToAnalyse)
            val dateOfWeekMapper = UiDateMapper(timezone, UILocalizer.getDateFormat(DateFormat.DAY_OF_WEEK))
            val dayOfWeek = dateOfWeekMapper.map(first.epochDateMills)
            DayWeatherModel(dayDate = dateMapper.map(first.epochDateMills),
                    humanDate = dayOfWeek,
                    tempMax = averageTempMax.toInt(),
                    tempMin = averageTempMin.toInt(),
                    iconId = averageIcon)
        }.values
    }

    private fun mapCurrentWeatherModel(source: List<WeatherWithPlace>, timezone: String, dateMapper: UiDateMapper): CurrentWeatherModel {
        val nowInMills = Date().time
        Log.d("CurrentP", "!!!mapCurrentWeatherModel---------------------------------------")
        source.forEach {
            val diff = abs(it.epochDateMills.time - nowInMills)
            Log.d("CurrentP", "dateSec ${it.dateSeconds} date ${it.dateTxt} diff $diff")
        }
        val value = source.minBy { abs(it.epochDateMills.time - nowInMills) } ?: source[0]

        return CurrentWeatherModel(
                placeName = value.placeName,
                description = value.description,
                humanDate = dateMapper.map(value.epochDateMills),
                tempMax = value.temperatureMax.toInt(),
                tempMin = value.temperatureMin.toInt(),
                currentTemperature = Temperature.middleTemperature(value.temperatureMin, value.temperatureMax).value.toInt(),
                iconId = value.iconId,
                snow = value.snow,
                cloudiness = value.cloudiness,
                rain = value.rain,
                hour24Format = DateBuilder(value.epochDateMills, timezone).getHour24Format(),
                isDay = isDay(value.iconId)
        )
    }

    private fun createDayMap(source: List<WeatherWithPlace>): HashMap<Int, MutableList<WeatherWithPlace>> {
        val dayToForecast: HashMap<Int, MutableList<WeatherWithPlace>> = HashMap()
        source.forEach { weather ->
            val day = DateBuilder(weather.epochDateMills).getDay()

            if (dayToForecast.containsKey(day)) {
                dayToForecast[day]!!.add(weather)
            } else {
                dayToForecast[day] = mutableListOf(weather)
            }
        }
        return dayToForecast
    }

    private fun calculateWeatherIcon(weathers: List<WeatherWithPlace>): String {
        if (weathers.isEmpty()) {
            return "01d"
        }
        val iconsIds: List<Int> = weathers.map { Integer.parseInt(it.iconId.substring(0, 2)) }
        val mostOccasionsIcon = iconsIds.groupingBy { it }.eachCount().maxBy { it.value }?.key ?: 1
        val formatter = DecimalFormat("00")
        val result = "${formatter.format(mostOccasionsIcon)}d"
        return result
    }

    companion object {
        fun isDay(iconId: String): Boolean {
            return iconId.last() == 'd'
        }
    }


}