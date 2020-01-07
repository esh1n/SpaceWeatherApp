package com.lab.esh1n.weather.weather.mapper

import com.esh1n.core_android.map.Mapper
import com.esh1n.utils_android.DateBuilder
import com.lab.esh1n.data.cache.TemperatureUnit
import com.lab.esh1n.data.cache.Units
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.weather.adapter.DayForecastSection
import com.lab.esh1n.weather.weather.adapter.DayOverallForecastModel
import com.lab.esh1n.weather.weather.adapter.DayWindForecastModel
import com.lab.esh1n.weather.weather.adapter.DaytimeForecastModel
import com.lab.esh1n.weather.weather.model.Temperature
import com.lab.esh1n.weather.weather.model.Wind

class DayForecastSectionsMapper(private val uiLocalizer: UiLocalizer) : Mapper<List<WeatherWithPlace>, List<Pair<DayForecastSection, List<DaytimeForecastModel>>>>() {
    override fun map(source: List<WeatherWithPlace>): List<Pair<DayForecastSection, List<DaytimeForecastModel>>> {
//TODO read all values by average not from first item found
        val firstWeather = source[0]
        val timezone = firstWeather.timezone
        val morning = source.findLast {
            val hour = DateBuilder(it.epochDateMills, timezone).getHour24Format()
            hour in 3..6
        }
        val day = source.find {
            val hour = DateBuilder(it.epochDateMills, timezone).getHour24Format()
            hour in 15..18
        }
        val evening = source.find {
            val hour = DateBuilder(it.epochDateMills, timezone).getHour24Format()
            hour in 18..21
        }
        val night = source.findLast {
            val hour = DateBuilder(it.epochDateMills, timezone).getHour24Format()
            hour in 21..24
        }
        //TODO include next day midnignt

        val finalMorning = morning ?: firstWeather
        val finalDay = day ?: firstWeather
        val finalEvening = evening ?: firstWeather
        val finalNight = night ?: firstWeather
        val mainSection = prepareMainSection(finalMorning, finalDay, finalEvening, finalNight)
        val windSection = prepareWindSection(finalMorning, finalDay, finalEvening, finalNight)
        return arrayListOf(mainSection, windSection)
    }

    private fun prepareWindSection(morning: WeatherWithPlace, day: WeatherWithPlace, evening: WeatherWithPlace, night: WeatherWithPlace): Pair<DayForecastSection, List<DayWindForecastModel>> {
        //TODO read WindFrom dao
        val windItems = arrayListOf(
                DayWindForecastModel(dayTime = R.string.title_morning, iconId = "wind",
                        wind = uiLocalizer.localizeWind(Wind(morning.windSpeed, Units.METRIC))),
                DayWindForecastModel(dayTime = R.string.title_day, iconId = "wind",
                        wind = uiLocalizer.localizeWind(Wind(day.windSpeed, Units.METRIC))),
                DayWindForecastModel(dayTime = R.string.title_evening, iconId = "wind",
                        wind = uiLocalizer.localizeWind(Wind(evening.windSpeed, Units.METRIC))),
                DayWindForecastModel(dayTime = R.string.title_night, iconId = "wind",
                        wind = uiLocalizer.localizeWind(Wind(night.windSpeed, Units.METRIC)))
        )
        return Pair(DayForecastSection.WIND, windItems)
    }

    private fun prepareMainSection(morning: WeatherWithPlace, day: WeatherWithPlace, evening: WeatherWithPlace, night: WeatherWithPlace): Pair<DayForecastSection, List<DayOverallForecastModel>> {
        //TODO read Temperature From dao
        val mainItems = arrayListOf(
                DayOverallForecastModel(dayTime = R.string.title_morning, iconId = morning.iconId,
                        temperature = uiLocalizer.localizeTemperature(Temperature(morning.temperature, TemperatureUnit.C))),
                DayOverallForecastModel(dayTime = R.string.title_day, iconId = day.iconId,
                        temperature = uiLocalizer.localizeTemperature(Temperature(day.temperature, TemperatureUnit.C))),
                DayOverallForecastModel(dayTime = R.string.title_evening, iconId = evening.iconId,
                        temperature = uiLocalizer.localizeTemperature(Temperature(evening.temperature, TemperatureUnit.C))),
                DayOverallForecastModel(dayTime = R.string.title_night, iconId = night.iconId,
                        temperature = uiLocalizer.localizeTemperature(Temperature(night.temperature, TemperatureUnit.C)))
        )
        return Pair(DayForecastSection.MAIN, mainItems)
    }


}