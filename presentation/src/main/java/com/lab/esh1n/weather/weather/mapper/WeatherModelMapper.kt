package com.lab.esh1n.weather.weather.mapper

import com.esh1n.utils_android.DateBuilder
import com.lab.esh1n.data.cache.AppPrefs
import com.lab.esh1n.data.cache.entity.SunsetSunriseTimezonePlaceEntry
import com.lab.esh1n.data.cache.entity.Temperature
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.data.cache.entity.WindSpeed
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.utils.StringResValueProperty
import com.lab.esh1n.weather.weather.model.*
import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.math.abs
import kotlin.math.roundToInt


class WeatherModelMapper(private val uiLocalizer: UiLocalizer, private val prefs: AppPrefs) {


    fun map(source: Pair<SunsetSunriseTimezonePlaceEntry, List<WeatherWithPlace>>): List<WeatherModel> {
        if (source.second.isEmpty()) {
            return emptyList()
        } else {
            val weathers = source.second.toMutableList()

            val firstWeather = weathers[0]
            val timezone = firstWeather.timezone
            val dateBuilder = DateBuilder(firstWeather.epochDateMills, timezone)
            val firstDay = dateBuilder.getDay()
            val nextDay = dateBuilder.nextDay().getDay()
            val firstDayForecast = getFirstDayForecast(weathers, timezone, firstDay, nextDay)
            weathers.removeAll(firstDayForecast)
            val dayToForecast = createDayMap(weathers, timezone)
            dayToForecast.remove(firstDay)

            val sunsetSunrise = source.first
            val currentWeatherModel = mapCurrentWeatherModel(sunsetSunrise, firstDayForecast.toMutableList(), dayToForecast[nextDay], timezone)
            val resultWeatherModel = mutableListOf<WeatherModel>()
            resultWeatherModel.add(currentWeatherModel)
            resultWeatherModel.addAll(mapOtherDay(dayToForecast, timezone, uiLocalizer.provideDateMapper(timezone, DateFormat.MONTH_DAY)))
            return resultWeatherModel
        }
    }

    private fun getFirstDayForecast(weathers: List<WeatherWithPlace>, timezone: String, firstDay: Int, nextDay: Int): List<WeatherWithPlace> {
        return weathers.filter {
            val db = DateBuilder(it.epochDateMills, timezone)
            val sameDay = db.getDay() == firstDay
            val midnight = (db.getDay() == nextDay) && db.getHour24Format() == 0
            sameDay || midnight
        }
    }

    private fun mapOtherDay(dayToForecast: HashMap<Int, MutableList<WeatherWithPlace>>, timezone: String, dateMapper: UiDateListMapper): Collection<DayWeatherModel> {
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
            val roundedAverageTempDay = AverageWeatherUtil.averageInt(dayWeathersToAnalyse.map { it.temperatureMax })
            val roundedAverageTempNight = AverageWeatherUtil.averageInt(nightWeathersToAnalyse.map { it.temperatureMin })
            val averageIconAndDescription = AverageWeatherUtil.calculateWeatherIconAndDescription(values)
            val dateOfWeekMapper = uiLocalizer.provideDateMapper(timezone, DateFormat.DAY_OF_WEEK)
            val dayOfWeek = dateOfWeekMapper.map(first.epochDateMills)
            val dayOfYear = DateBuilder(first.epochDateMills, timezone).getDayOfYear()
            DayWeatherModel(dayDate = dateMapper.map(first.epochDateMills),
                    humanDate = dayOfWeek,
                    tempMax = roundedAverageTempDay,
                    tempMin = roundedAverageTempNight,
                    iconId = averageIconAndDescription.first,
                    description = averageIconAndDescription.second,
                    dayOFTheYear = dayOfYear)
        }.values
    }

    private fun mapCurrentWeatherModel(sunsetSunriseTimezone: SunsetSunriseTimezonePlaceEntry, firstDay: MutableList<WeatherWithPlace>, secondDay: MutableList<WeatherWithPlace>?, timezone: String): CurrentWeatherModel {
        val nowInMills = DateBuilder(Date(), timezone).build().time
        val now = firstDay.minBy { abs(it.epochDateMills.time - nowInMills) } ?: firstDay[0]
        firstDay.remove(now)

        val dayHourDateMapper = uiLocalizer.provideDateMapper(timezone, DateFormat.DAY_HOUR)
        // currentTemperature = Temperature.middleTemperature(now.temperatureMin, now.temperatureMax).getHumanReadable(),
        val hourWeathers = mapHourWeathers(timezone, sunsetSunriseTimezone.sunset, sunsetSunriseTimezone.sunrise, firstDay, secondDay
                ?: arrayListOf(), now)
        val dayOfTheYear = DateBuilder(now.epochDateMills, timezone).getDayOfYear()
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
                hourWeatherEvents = hourWeathers,
                dayOfTheYear = dayOfTheYear
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

        val hourWeathers = HourWeatherEventListMapper(isDay, dateHourMapper, dateHourAndDayMapper
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

        val localixedWind = uiLocalizer.localizeWindSpeed(WindSpeed(now.windSpeed.toDouble(), prefs.getUnits()))
        hourWeathers.add(0, HeaderHourWeatherModel(isDay, now.pressure, localixedWind, now.humidity, now.epochDateMills))
        return hourWeathers
    }


    private fun insertItem(timezone: String, isDay: Boolean, dateHourMapper: UiDateListMapper, dateHourDayMapper: UiDateListMapper, name: Int, iconId: String, date: Date, list: MutableList<HourWeatherModel>) {
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

    private fun createDayMap(source: List<WeatherWithPlace>, timezone: String): HashMap<Int, MutableList<WeatherWithPlace>> {
        val dayToForecast: HashMap<Int, MutableList<WeatherWithPlace>> = LinkedHashMap()
        source.forEach { weather ->
            val dateBuilder = DateBuilder(weather.epochDateMills, timezone)
            val hour = dateBuilder.getHour24Format()
            val isNightOfPreviousDay = hour <= END_NIGHT_HOUR;
            val day = dateBuilder.getDay()
            val finalDay = if (isNightOfPreviousDay) dateBuilder.previousDay().getDay() else day

            if (dayToForecast.containsKey(finalDay)) {
                dayToForecast[finalDay]!!.add(weather)
            } else {
                dayToForecast[finalDay] = mutableListOf(weather)
            }
        }
        return dayToForecast
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