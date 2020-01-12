package com.lab.esh1n.weather.weather.mapper

import com.esh1n.core_android.map.Mapper
import com.esh1n.utils_android.DateBuilder
import com.lab.esh1n.data.cache.TemperatureUnit
import com.lab.esh1n.data.cache.Units
import com.lab.esh1n.data.cache.entity.Temperature
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.data.cache.entity.WindDegree
import com.lab.esh1n.data.cache.entity.WindSpeed
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.weather.adapter.DayForecastSection
import com.lab.esh1n.weather.weather.adapter.DayOverallForecastModel
import com.lab.esh1n.weather.weather.adapter.DayWindForecastModel
import com.lab.esh1n.weather.weather.adapter.DaytimeForecastModel

class DayForecastSectionsMapper(private val uiLocalizer: UiLocalizer) : Mapper<List<WeatherWithPlace>, List<Pair<DayForecastSection, List<DaytimeForecastModel>>>>() {
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
        return arrayListOf(mainSection, windSection)
    }


    private fun prepareWindSection(titles: List<Int>, morningDayEveningNight: List<List<WeatherWithPlace>>): Pair<DayForecastSection, List<DayWindForecastModel>> {
        //TODO read WindFrom dao
        val windItems = arrayListOf<DayWindForecastModel>()
        morningDayEveningNight.forEachIndexed { index, list ->
            val averageSpeed = AverageWeatherUtil.average(list.map { it.windSpeed })
            val averageWindDirection = AverageWeatherUtil.averageWindDirection(list.map { WindDegree(it.windDegree) })
            windItems.add(DayWindForecastModel(dayTime = titles[index], iconId = "wind",
                    windSpeed = uiLocalizer.localizeWindSpeed(WindSpeed(averageSpeed, Units.METRIC)),
                    windDirecton = uiLocalizer.localizaWindDirection(averageWindDirection)))
        }
        return Pair(DayForecastSection.WIND, windItems)
    }

    private fun prepareMainSection(titles: List<Int>, morningDayEveningNight: List<List<WeatherWithPlace>>): Pair<DayForecastSection, List<DayOverallForecastModel>> {
        //TODO read Temperature From dao
        val mainItems = arrayListOf<DayOverallForecastModel>()
        morningDayEveningNight.forEachIndexed { index, list ->
            val averageIconAndDescription = AverageWeatherUtil.calculateWeatherIconAndDescription(list)
            val averageTemperature = AverageWeatherUtil.average(list.map { it.temperature })
            mainItems.add(DayOverallForecastModel(dayTime = titles[index], iconId = averageIconAndDescription.first,
                    temperature = uiLocalizer.localizeTemperature(Temperature(averageTemperature, TemperatureUnit.C))))
        }
        return Pair(DayForecastSection.MAIN, mainItems)
    }


}