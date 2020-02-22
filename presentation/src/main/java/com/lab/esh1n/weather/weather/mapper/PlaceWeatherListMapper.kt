package com.lab.esh1n.weather.weather.mapper

import com.esh1n.core_android.map.Mapper
import com.esh1n.utils_android.DateBuilder
import com.esh1n.utils_android.country.Country
import com.lab.esh1n.data.cache.entity.PlaceWithCurrentWeatherEntry
import com.lab.esh1n.weather.weather.model.NoDataBackgroundModel
import com.lab.esh1n.weather.weather.model.PlaceModel
import com.lab.esh1n.weather.weather.model.SimpleBackgroundModel
import java.util.*

class PlaceWeatherListMapper(private val uiLocalizer: UiLocalizer) : Mapper<PlaceWithCurrentWeatherEntry, PlaceModel>() {
    //TODO refactor getting isDay
    override fun map(source: PlaceWithCurrentWeatherEntry): PlaceModel {
        val uiDateMapper = uiLocalizer.provideDateMapper(source.timezone, DateFormat.HOUR)
        val dateBuilder = DateBuilder(source.date ?: Date(), source.timezone)
        val isDay = if (source.iconId == null) dateBuilder.getHour24Format() in 9..18 else WeatherModelMapper.isDay(source.iconId!!)
        val weatherBackgroundModel = if (source.iconId == null) NoDataBackgroundModel(isDay = isDay) else SimpleBackgroundModel(source.iconId
                ?: "00d", isDay = isDay, hourOfDay = DateBuilder(source.date
                ?: Date(), source.timezone).getHour24Format(),
                rain = source.rain ?: 0, clouds = source.cloudiness ?: 0, snow = source.snow ?: 0)
        return PlaceModel(
                name = source.placeName,
                id = source.id,
                iconId = source.iconId ?: "00d",
                time = uiDateMapper.map(dateBuilder.build()),
                temperature = source.temperatureMax,
                weatherBackgroundModel = weatherBackgroundModel,
                weatherDescription = source.weatherDescription ?: "",
                countryFlag = Country.getCountryByISO(source.countryCode).flag
        )
    }

}