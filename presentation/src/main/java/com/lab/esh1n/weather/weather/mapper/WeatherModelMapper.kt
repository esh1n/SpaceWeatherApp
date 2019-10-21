package com.lab.esh1n.weather.weather.mapper

import com.esh1n.utils_android.DateBuilder
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.weather.model.*
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.abs


class WeatherModelMapper(private val uiLocalizer: UiLocalizer) {



    fun map(source: List<WeatherWithPlace>): List<WeatherModel> {
        if (source.isEmpty()) {
            return emptyList()
        } else {
            val firstWeather = source[0]
            val timezone = firstWeather.timezone
            val firstDay = DateBuilder(firstWeather.epochDateMills).getDay()
            val dateMapper = uiLocalizer.provideDateMapper(timezone, DateFormat.MONTH_DAY)
            val dayToForecast = createDayMap(source)
            val firstDayForecasts = dayToForecast[firstDay]
            dayToForecast.remove(firstDay)
            val currentWeatherModel = mapCurrentWeatherModel(firstDayForecasts
                    ?: arrayListOf(), timezone)
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
            val dateOfWeekMapper = uiLocalizer.provideDateMapper(timezone, DateFormat.DAY_OF_WEEK)
            val dayOfWeek = dateOfWeekMapper.map(first.epochDateMills)
            DayWeatherModel(dayDate = dateMapper.map(first.epochDateMills),
                    humanDate = dayOfWeek,
                    tempMax = averageTempMax.toInt(),
                    tempMin = averageTempMin.toInt(),
                    iconId = averageIcon)
        }.values
    }

    private fun mapCurrentWeatherModel(source: MutableList<WeatherWithPlace>, timezone: String): CurrentWeatherModel {
        val nowInMills = Date().time
        val now = source.minBy { abs(it.epochDateMills.time - nowInMills) } ?: source[0]
        source.remove(now)

        val isDay = isDay(now.iconId)

        val dayHourDateMapper = uiLocalizer.provideDateMapper(timezone, DateFormat.DAY_HOUR)
        val dateHourMapper = uiLocalizer.provideDateMapper(timezone, DateFormat.HOUR)


        val hourWeathers = HourWeatherEventMapper(isDay, dateHourMapper
        ) { valueFromDb ->
            uiLocalizer.localizeTemperature(Temperature(valueFromDb))
        }.map(source).toMutableList()
        hourWeathers.add(0, HeaderHourWeatherModel(isDay, "Current", now.pressure, now.windDegree, now.humidity))
        return CurrentWeatherModel(
                placeName = now.placeName,
                description = now.description,
                humanDate = dayHourDateMapper.map(now.epochDateMills),
                tempMax = now.temperatureMax.toInt(),
                tempMin = now.temperatureMin.toInt(),
                currentTemperature = Temperature.middleTemperature(now.temperatureMin, now.temperatureMax).getHumanReadable(),
                iconId = now.iconId,
                snow = now.snow,
                cloudiness = now.cloudiness,
                rain = now.rain,
                hour24Format = DateBuilder(now.epochDateMills, timezone).getHour24Format(),
                isDay = isDay,
                hourWeatherEvents = hourWeathers
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
        return "${formatter.format(mostOccasionsIcon)}d"
    }

    companion object {
        fun isDay(iconId: String): Boolean {
            return iconId.last() == 'd'
        }
    }


}