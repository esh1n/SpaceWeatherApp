package com.lab.esh1n.weather.presentation.mapper

import com.esh1n.utils_android.DateBuilder
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.data.cache.entity.SunsetSunriseTimezonePlaceEntry
import com.lab.esh1n.weather.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.data.cache.entity.WindSpeed
import com.lab.esh1n.weather.domain.IUILocalisator
import com.lab.esh1n.weather.domain.prefs.IPrefsInteractor
import com.lab.esh1n.weather.presentation.model.*
import com.lab.esh1n.weather.utils.StringResValueProperty
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt


class WeatherModelMapper(
    private val uiLocalize: IUILocalisator,
    private val prefs: IPrefsInteractor
) {


    fun map(
        sunsetSunrise: SunsetSunriseTimezonePlaceEntry,
        weathers: List<WeatherWithPlace>
    ): List<WeatherModel> {

        val firstWeather = weathers[0]
        val timezone = firstWeather.timezone

        val (firstDayInt, nextDayInt) = getTodayAndTomorrow(firstWeather, timezone)
        val firstDayForecast = getFirstDayForecast(weathers, timezone, firstDayInt, nextDayInt)
        val now = getNow(firstDayForecast, timezone)
        val dayToForecast = createDayMap(
            weathers.filterNot { firstDayForecast.contains(it) },
            firstDayInt,
            timezone
        )
        val nextDayForecasts = dayToForecast[nextDayInt].orEmpty()

        val hourWeatherEvents: List<HourWeatherModel> = mapHourWeathers(
            timezone,
            sunsetSunrise,
            firstDayForecast.union(nextDayForecasts).filterNot { it == now },
            now
        )
        return mutableListOf<WeatherModel>().apply {

            add(mapCurrentWeatherModel(now, hourWeatherEvents, timezone))
            addAll(
                mapOtherDay(
                    dayToForecast,
                    timezone,
                    uiLocalize.provideDateMapper(timezone, DateFormat.MONTH_DAY)
                )
            )

        }

    }

    private fun getTodayAndTomorrow(
        firstWeather: WeatherWithPlace,
        timezone: String
    ): Pair<Int, Int> {
        val dateBuilder = DateBuilder(firstWeather.epochDateMills, timezone)
        val firstDay = dateBuilder.getDay()
        val nextDay = dateBuilder.nextDay().getDay()
        return Pair(firstDay, nextDay)
    }

    private fun getNow(
        firstDayForecasts: List<WeatherWithPlace>,
        timezone: String
    ): WeatherWithPlace {
        val nowInMills = DateBuilder(Date(), timezone).build().time
        return firstDayForecasts.minByOrNull { abs(it.epochDateMills.time - nowInMills) }
            ?: firstDayForecasts[0]
    }

    private fun getFirstDayForecast(
        weathers: List<WeatherWithPlace>,
        timezone: String,
        firstDay: Int,
        nextDay: Int
    ): List<WeatherWithPlace> {
        return weathers.filter {
            val db = DateBuilder(it.epochDateMills, timezone)
            val sameDay = db.getDay() == firstDay
            val midnight = (db.getDay() == nextDay) && db.getHour24Format() == 0
            sameDay || midnight
        }
    }

    private fun mapOtherDay(
        dayToForecast: Map<Int, MutableList<WeatherWithPlace>>,
        timezone: String,
        dateMapper: UiDateListMapper
    ): Collection<DayWeatherModel> {
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
                    val nightHour =
                        hourOfDay in START_NIGHT_HOUR..24 || hourOfDay in 0..END_NIGHT_HOUR
                    nightHour
                }
            val roundedAverageTempDay =
                AverageWeatherUtil.averageInt(dayWeathersToAnalyse.map { it.temperatureMax.value })
            val roundedAverageTempNight =
                AverageWeatherUtil.averageInt(nightWeathersToAnalyse.map { it.temperatureMin.value })
            val averageIconAndDescription =
                AverageWeatherUtil.calculateWeatherIconAndDescription(values)
            val dateOfWeekMapper = uiLocalize.provideDateMapper(timezone, DateFormat.DAY_OF_WEEK)
            val dayOfWeek = dateOfWeekMapper.map(first.epochDateMills)
            val dayOfYear = DateBuilder(first.epochDateMills, timezone).getDayOfYear()
            DayWeatherModel(
                dayDate = dateMapper.map(first.epochDateMills),
                humanDate = dayOfWeek,
                tempMax = roundedAverageTempDay,
                tempMin = roundedAverageTempNight,
                iconId = averageIconAndDescription.first,
                description = averageIconAndDescription.second,
                dayOfTheYear = dayOfYear
            )
        }.values
    }

    private fun mapCurrentWeatherModel(
        now: WeatherWithPlace,
        hourWeatherEvents: List<HourWeatherModel>,
        timezone: String
    ): CurrentWeatherModel {

        val dayHourDateMapper = uiLocalize.provideDateMapper(timezone, DateFormat.DAY_HOUR)
        // currentTemperature = Temperature.middleTemperature(now.temperatureMin, now.temperatureMax).getHumanReadable(),
        val dayOfTheYear = DateBuilder(now.epochDateMills, timezone).getDayOfYear()
        return CurrentWeatherModel(
            placeName = now.placeName,
            description = now.description,
            humanDate = dayHourDateMapper.map(now.epochDateMills),
            tempMax = now.temperatureMax.value.roundToInt(),
            tempMin = now.temperatureMin.value.roundToInt(),
            iconId = now.iconId,
            snow = now.snow,
            cloudiness = now.cloudiness,
            rain = now.rain,
            hour24Format = DateBuilder(now.epochDateMills, timezone).getHour24Format(),
            isDay = isDay(now.iconId),
            hourWeatherEvents = hourWeatherEvents,
            dayOfTheYear = dayOfTheYear
        )
    }

    private fun mapHourWeathers(
        timezone: String,
        sunsetSunrise: SunsetSunriseTimezonePlaceEntry,
        weatherItems: List<WeatherWithPlace>,
        now: WeatherWithPlace
    ): List<HourWeatherModel> {
        val (_, sunsetDate, sunriseDate, _) = sunsetSunrise
        val dateHourMapper = uiLocalize.provideDateMapper(timezone, DateFormat.HOUR)
        val dateHourAndDayMapper = uiLocalize.provideDateMapper(timezone, DateFormat.HOUR_DAY)
        val isDay = isDay(now.iconId)
        val twoDayWeathers = weatherItems.filter { it.epochDateMills.after(now.epochDateMills) }

        val hourWeathers =
            HourWeatherEventListMapper(isDay, dateHourMapper, dateHourAndDayMapper) { valueFromDb ->
                uiLocalize.localizeTemperature(valueFromDb)
            }.map(twoDayWeathers).toMutableList()
        if (sunsetDate.after(now.epochDateMills)) {
            insertItem(
                timezone,
                isDay,
                dateHourMapper,
                dateHourAndDayMapper,
                R.string.text_sunset,
                "sunset",
                sunsetDate,
                hourWeathers
            )
        }
        if (sunriseDate.after(now.epochDateMills)) {
            insertItem(
                timezone,
                isDay,
                dateHourMapper,
                dateHourAndDayMapper,
                R.string.text_sunrise,
                "sunrise",
                sunriseDate,
                hourWeathers
            )
        }
        val nextDaySunrise = DateBuilder(sunriseDate, timezone).plusDays(1).build()
        insertItem(
            timezone,
            isDay,
            dateHourMapper,
            dateHourAndDayMapper,
            R.string.text_sunrise,
            "sunrise",
            nextDaySunrise,
            hourWeathers
        )
        val nextDaySunset = DateBuilder(sunsetDate, timezone).plusDays(1).build()
        insertItem(
            timezone,
            isDay,
            dateHourMapper,
            dateHourAndDayMapper,
            R.string.text_sunset,
            "sunset",
            nextDaySunset,
            hourWeathers
        )

        val localizedWind =
            uiLocalize.localizeWindSpeed(WindSpeed(now.windSpeed.value, prefs.getMeasureUnits()))
        hourWeathers.add(
            0,
            HeaderHourWeatherModel(
                isDay,
                now.pressure,
                localizedWind,
                now.humidity,
                now.epochDateMills
            )
        )
        return hourWeathers
    }


    private fun insertItem(
        timezone: String,
        isDay: Boolean,
        dateHourMapper: UiDateListMapper,
        dateHourDayMapper: UiDateListMapper,
        name: Int,
        iconId: String,
        date: Date,
        list: MutableList<HourWeatherModel>
    ) {
        val position = list.indexOfFirst { weather ->
            weather.date.after(date)
        }
        if (position != -1) {
            val isToday = DateBuilder(date, timezone).isSameDay(Date())
            val dateMapper = if (isToday) dateHourMapper else dateHourDayMapper
            val item = SimpleHourWeatherModel(
                isDay,
                dateMapper.map(date),
                iconId,
                StringResValueProperty(name),
                date
            )
            list.add(position, item)
        }
    }

    private fun createDayMap(
        source: List<WeatherWithPlace>,
        firstDayInt: Int,
        timezone: String
    ): Map<Int, MutableList<WeatherWithPlace>> {
        val dayToForecast: HashMap<Int, MutableList<WeatherWithPlace>> = LinkedHashMap()
        source.forEach { weather ->
            val dateBuilder = DateBuilder(weather.epochDateMills, timezone)
            val hour = dateBuilder.getHour24Format()
            val isNightOfPreviousDay = hour <= END_NIGHT_HOUR
            val day = dateBuilder.getDay()
            val finalDay = if (isNightOfPreviousDay) dateBuilder.previousDay().getDay() else day

            if (dayToForecast.containsKey(finalDay)) {
                dayToForecast[finalDay]?.add(weather)
            } else {
                dayToForecast[finalDay] = mutableListOf(weather)
            }
        }
        return dayToForecast.filterNot { it.key == firstDayInt }
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