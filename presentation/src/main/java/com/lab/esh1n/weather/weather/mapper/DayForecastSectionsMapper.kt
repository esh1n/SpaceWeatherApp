package com.lab.esh1n.weather.weather.mapper

import com.esh1n.core_android.map.Mapper
import com.esh1n.utils_android.DateBuilder
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.data.cache.entity.Temperature
import com.lab.esh1n.weather.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.data.cache.entity.WindSpeed
import com.lab.esh1n.weather.domain.IUILocalisator
import com.lab.esh1n.weather.domain.prefs.TemperatureUnit
import com.lab.esh1n.weather.domain.prefs.Units
import com.lab.esh1n.weather.utils.OneValueProperty
import com.lab.esh1n.weather.utils.StringResValueProperty
import com.lab.esh1n.weather.weather.adapter.*

class DayForecastSectionsMapper(private val uiLocalizer: IUILocalisator) :
    Mapper<List<WeatherWithPlace>, List<Pair<DayForecastSection, List<DaytimeForecastModel>>>>() {
    override fun map(source: List<WeatherWithPlace>): List<Pair<DayForecastSection, List<DaytimeForecastModel>>> {
        val firstWeather = source[0]
        val timezone = firstWeather.timezone
        val morning = source.filter {
            val hour = DateBuilder(it.epochDateMills, timezone).getHour24Format()
            hour in 4..9
        }
        val day = source.filter {
            val hour = DateBuilder(it.epochDateMills, timezone).getHour24Format()
            hour in 12..18
        }
        val evening = source.filter {
            val hour = DateBuilder(it.epochDateMills, timezone).getHour24Format()
            hour in 18..21
        }
        val night = source.filter {
            val hour = DateBuilder(it.epochDateMills, timezone).getHour24Format()
            hour !in 4..21
        }

        val titles = listOf(R.string.title_morning, R.string.title_day, R.string.title_evening, R.string.title_night)
        val morningDayEveningNight = listOf(morning, day, evening, night)
        val mainSection = prepareMainSection(titles, morningDayEveningNight)
        val windSection = prepareWindSection(titles, morningDayEveningNight)
        val pressureSection = preparePressureSection(titles, morningDayEveningNight)
        val humiditySection = prepareHumiditySection(titles, morningDayEveningNight)
        return arrayListOf(mainSection, windSection, humiditySection, pressureSection)
    }


    private fun prepareWindSection(titles: List<Int>, morningDayEveningNight: List<List<WeatherWithPlace>>): Pair<DayForecastSection, List<DayWindForecastModel>> {
        val windItems = arrayListOf<DayWindForecastModel>()
        morningDayEveningNight.forEachIndexed { index, list ->
            val averageSpeed = AverageWeatherUtil.average(list.map { it.windSpeed.value })
            val windDegrees = list.map { it.windDegree }
            val averageWindDirection = AverageWeatherUtil.averageWindDirection(windDegrees)
            val degreesWithAverageDirection = windDegrees.filter { it.direction == averageWindDirection }.map { it.degree }
            val averageWindDegree = AverageWeatherUtil.average(degreesWithAverageDirection)
            windItems.add(DayWindForecastModel(
                dayTime = StringResValueProperty(titles[index]), iconId = "wind",
                windSpeed = uiLocalizer.localizeWindSpeed(WindSpeed(averageSpeed, Units.METRIC)),
                windDirection = uiLocalizer.localizeWindDirection(averageWindDirection),
                windDegree = averageWindDegree.toFloat()
            ))
        }
        return Pair(DayForecastSection.WIND, windItems)
    }

    private fun prepareHumiditySection(titles: List<Int>, morningDayEveningNight: List<List<WeatherWithPlace>>): Pair<DayForecastSection, List<DayHumidityForecastModel>> {
        val humidityItems = arrayListOf<DayHumidityForecastModel>()
        morningDayEveningNight.forEachIndexed { index, list ->
            val averageHumidity = AverageWeatherUtil.averageInt(list.map { it.humidity.toDouble() })
            humidityItems.add(DayHumidityForecastModel(
                    dayTime = StringResValueProperty(titles[index]),
                    humidity = OneValueProperty(R.string.humidity_percents, averageHumidity.toString())))
        }
        return Pair(DayForecastSection.HUMIDITY, humidityItems)
    }

    private fun preparePressureSection(titles: List<Int>, morningDayEveningNight: List<List<WeatherWithPlace>>): Pair<DayForecastSection, List<DayPressureForecastModel>> {
        val pressureItems = arrayListOf<DayPressureForecastModel>()
        morningDayEveningNight.forEachIndexed { index, list ->
            val averagePressure = AverageWeatherUtil.averageInt(list.map { it.pressure.toDouble() })
            pressureItems.add(DayPressureForecastModel(
                    dayTime = StringResValueProperty(titles[index]),
                    pressure = OneValueProperty(R.string.pressure_pa, averagePressure.toString())))
        }
        return Pair(DayForecastSection.PRESSURE, pressureItems)
    }

    private fun prepareMainSection(titles: List<Int>, morningDayEveningNight: List<List<WeatherWithPlace>>): Pair<DayForecastSection, List<DayOverallForecastModel>> {
        val mainItems = arrayListOf<DayOverallForecastModel>()
        morningDayEveningNight.forEachIndexed { index, list ->
            val averageIconAndDescription = AverageWeatherUtil.calculateWeatherIconAndDescription(list)
            val averageTemperature = AverageWeatherUtil.average(list.map { it.temperature.value })
            val updatedDescription = joinString(averageIconAndDescription.second.split(" "))
            mainItems.add(DayOverallForecastModel(
                    dayTime = StringResValueProperty(titles[index]),
                    iconId = averageIconAndDescription.first,
                    description = updatedDescription,
                    temperature = uiLocalizer.localizeTemperature(Temperature(averageTemperature, TemperatureUnit.C))))
        }
        return Pair(DayForecastSection.MAIN, mainItems)
    }

    private fun joinString(items: List<String>): String {
        if (items.isEmpty()) {
            return ""
        }
        val builder = StringBuilder(items[0])
        if (items.size != 1) {
            val otherItems = items.subList(1, items.size)
            otherItems.forEach {
                val delimiter = if (it.length < 2) " " else "\n"
                builder.append("$delimiter$it")
            }
        }

        return builder.toString()
    }


}