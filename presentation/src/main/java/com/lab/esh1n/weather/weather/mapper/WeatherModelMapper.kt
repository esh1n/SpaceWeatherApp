package com.lab.esh1n.weather.weather.mapper

import com.esh1n.utils_android.DateBuilder
import com.lab.esh1n.data.cache.entity.UpdatePlaceEntry
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.utils.StringResValueProperty
import com.lab.esh1n.weather.weather.model.*
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.abs
import kotlin.math.roundToInt


class WeatherModelMapper(private val uiLocalizer: UiLocalizer) {


    fun map(source: Pair<UpdatePlaceEntry, List<WeatherWithPlace>>): List<WeatherModel> {
        if (source.second.isEmpty()) {
            return emptyList()
        } else {
            val weathers = source.second
            val sunsetSunrise = source.first
            val firstWeather = weathers[0]
            val timezone = firstWeather.timezone
            val firstDay = DateBuilder(firstWeather.epochDateMills, timezone).getDay()
            //TODO count first day weathers separately from other weathers
            val dayToForecast = createDayMap(firstDay, weathers, timezone)
            val firstDayForecast = dayToForecast[firstDay]
            dayToForecast.remove(firstDay)
            val currentWeatherModel = mapCurrentWeatherModel(sunsetSunrise, firstDayForecast
                    ?: arrayListOf(), dayToForecast[firstDay + 1] ?: arrayListOf(), timezone)
            val resultWeatherModel = mutableListOf<WeatherModel>()
            resultWeatherModel.add(currentWeatherModel)
            resultWeatherModel.addAll(mapOtherDay(dayToForecast, timezone, uiLocalizer.provideDateMapper(timezone, DateFormat.MONTH_DAY)))
            return resultWeatherModel
        }
    }

    private fun mapOtherDay(dayToForecast: HashMap<Int, MutableList<WeatherWithPlace>>, timezone: String, dateMapper: UiDateMapper): Collection<DayWeatherModel> {
        return dayToForecast.mapValues { (_, values) ->
            val first = values[0]
            val dayWeathersToAnalyse = values
                    .filter {
                        val hourOfDay = DateBuilder(it.epochDateMills, it.timezone).getHour24Format()
                        hourOfDay in START_DAY_HOUR..END_DAY_HOUR
                    }
            val nightWeathersToAnalyse = values
                    .filter {
                        val hourOfDay = DateBuilder(it.epochDateMills, it.timezone).getHour24Format()
                        val nightHour = hourOfDay in START_NIGHT_HOUR..24 || hourOfDay in 0..END_NIGHT_HOUR
                        nightHour
                    }
            val averageTempDay: Double = dayWeathersToAnalyse.map { it.temperatureMax }.average()
            val averageTempNight: Double = nightWeathersToAnalyse.map { it.temperatureMin }.average()
            val roundedAverageTempDay = if (averageTempDay.isNaN()) 0 else averageTempDay.roundToInt()
            val roundedAverageTempNight = if (averageTempNight.isNaN()) 0 else averageTempNight.roundToInt()
            val averageIconAndDescription = calculateWeatherIconAndDescription(values)
            val dateOfWeekMapper = uiLocalizer.provideDateMapper(timezone, DateFormat.DAY_OF_WEEK)
            val dayOfWeek = dateOfWeekMapper.map(first.epochDateMills)
            DayWeatherModel(dayDate = dateMapper.map(first.epochDateMills),
                    humanDate = dayOfWeek,
                    tempMax = roundedAverageTempDay,
                    tempMin = roundedAverageTempNight,
                    iconId = averageIconAndDescription.first,
                    description = averageIconAndDescription.second)
        }.values
    }

    private fun mapCurrentWeatherModel(sunsetSunrise: UpdatePlaceEntry, firstDay: MutableList<WeatherWithPlace>, secondDay: MutableList<WeatherWithPlace>, timezone: String): CurrentWeatherModel {
        val nowInMills = Date().time
        val now = firstDay.minBy { abs(it.epochDateMills.time - nowInMills) } ?: firstDay[0]
        firstDay.remove(now)


        val dayHourDateMapper = uiLocalizer.provideDateMapper(timezone, DateFormat.DAY_HOUR)
// currentTemperature = Temperature.middleTemperature(now.temperatureMin, now.temperatureMax).getHumanReadable(),
        val hourWeathers = mapHourWeathers(timezone, sunsetSunrise.sunset, sunsetSunrise.sunrise, firstDay, secondDay, now)
        return CurrentWeatherModel(
                placeName = now.placeName,
                description = now.description,
                humanDate = dayHourDateMapper.map(now.epochDateMills),
                tempMax = now.temperatureMax.roundToInt(),
                tempMin = now.temperatureMin.roundToInt(),
                iconId = now.iconId,
                snow = now.snow,
                cloudiness = now.cloudiness,
                rain = now.rain,
                hour24Format = DateBuilder(now.epochDateMills, timezone).getHour24Format(),
                isDay = isDay(now.iconId),
                hourWeatherEvents = hourWeathers
        )
    }

    private fun mapHourWeathers(timezone: String, sunsetDate: Date, sunriseDate: Date, firstDay: MutableList<WeatherWithPlace>, secondDay: MutableList<WeatherWithPlace>, now: WeatherWithPlace): List<HourWeatherModel> {
        val dateHourMapper = uiLocalizer.provideDateMapper(timezone, DateFormat.HOUR)
        val dateHourAndDayMapper = uiLocalizer.provideDateMapper(timezone, DateFormat.HOUR_DAY)
        val isDay = isDay(now.iconId)
        val twoDayWeathers = firstDay.apply {
            addAll(secondDay)
        }.filter { it.epochDateMills.after(now.epochDateMills) }
        //TODO get rid of isDay, move it to adapter
        //TODO make temperature default from dao

        val hourWeathers = HourWeatherEventMapper(isDay, dateHourMapper, dateHourAndDayMapper
        ) { valueFromDb ->
            uiLocalizer.localizeTemperature(Temperature(valueFromDb))
        }.map(twoDayWeathers).toMutableList()
        if (sunsetDate.after(now.epochDateMills)) {
            insertItem(timezone, isDay, dateHourMapper, dateHourAndDayMapper, R.string.text_sunset, "sunset", sunsetDate, hourWeathers)
        }
        if (sunriseDate.after(now.epochDateMills)) {
            insertItem(timezone, isDay, dateHourMapper, dateHourAndDayMapper, R.string.text_sunrise, "sunrise", sunriseDate, hourWeathers)
        }
        val nextDaySunrise = DateBuilder(sunriseDate, timezone).plusDays(1).build()
        insertItem(timezone, isDay, dateHourMapper, dateHourAndDayMapper, R.string.text_sunrise, "sunrise", nextDaySunrise, hourWeathers)
        val nextDaySunset = DateBuilder(sunsetDate, timezone).plusDays(1).build()
        insertItem(timezone, isDay, dateHourMapper, dateHourAndDayMapper, R.string.text_sunset, "sunset", nextDaySunset, hourWeathers)

        hourWeathers.add(0, HeaderHourWeatherModel(isDay, now.pressure, now.windDegree, now.humidity, now.epochDateMills))
        return hourWeathers
    }


    private fun insertItem(timezone: String, isDay: Boolean, dateHourMapper: UiDateMapper, dateHourDayMapper: UiDateMapper, name: Int, iconId: String, date: Date, list: MutableList<HourWeatherModel>) {
        val position = list.indexOfFirst { weather ->
            weather.date.after(date)
        }
        if (position != -1) {
            val isToday = DateBuilder(date, timezone).isSameDay(Date())
            val dateMapper = if (isToday) dateHourMapper else dateHourDayMapper
            val item = SimpleHourWeatherModel(isDay, dateMapper.map(date), iconId, StringResValueProperty(name), date)
            list.add(position, item)
        }
    }

    private fun createDayMap(firstDay: Int, source: List<WeatherWithPlace>, timezone: String): HashMap<Int, MutableList<WeatherWithPlace>> {
        val dayToForecast: HashMap<Int, MutableList<WeatherWithPlace>> = HashMap()
        source.forEach { weather ->
            val dateBuilder = DateBuilder(weather.epochDateMills, timezone)
            val hour = dateBuilder.getHour24Format()
            val isNightOfPreviousDay = hour <= END_NIGHT_HOUR;
            val day = dateBuilder.getDay()
            val finalDay = if (day != firstDay && isNightOfPreviousDay) day - 1 else day

            if (dayToForecast.containsKey(finalDay)) {
                dayToForecast[finalDay]!!.add(weather)
            } else {
                dayToForecast[finalDay] = mutableListOf(weather)
            }
        }
        return dayToForecast
    }

    private fun calculateWeatherIconAndDescription(weathers: List<WeatherWithPlace>): Pair<String, String> {
        if (weathers.isEmpty()) {
            return Pair("01d", "n/a")
        }
        val iconsIds: List<Int> = weathers.map { Integer.parseInt(it.iconId.substring(0, 2)) }
        val mostOccasionsIcon = iconsIds.groupingBy { it }.eachCount().maxBy { it.value }?.key ?: 1
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

    private fun getIconCode(value: Int, isDay: Boolean): String {
        val formatter = DecimalFormat("00")
        val dayOrNightChar = if (isDay) 'd' else 'n'
        return "${formatter.format(value)}$dayOrNightChar"
    }

    companion object {
        fun isDay(iconId: String): Boolean {
            return iconId.last() == 'd'
        }

        const val START_NIGHT_HOUR = 22
        const val END_NIGHT_HOUR = 6
        const val START_DAY_HOUR = 8
        const val END_DAY_HOUR = 21
    }


}