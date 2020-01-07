package com.lab.esh1n.weather.weather.mapper

import com.esh1n.core_android.map.ListMapper
import com.esh1n.utils_android.DateBuilder
import com.lab.esh1n.data.cache.entity.PlaceWithCurrentWeatherEntry
import com.lab.esh1n.weather.weather.model.PlaceModel
import com.lab.esh1n.weather.weather.model.WeatherBackgroundModel

class PlaceWeatherListMapper(private val uiLocalizer: UiLocalizer) : ListMapper<PlaceWithCurrentWeatherEntry, PlaceModel>() {
    override fun map(source: PlaceWithCurrentWeatherEntry): PlaceModel {
        val uiDateMapper = uiLocalizer.provideDateMapper(source.timezone, DateFormat.HOUR)
        return PlaceModel(
                name = source.placeName,
                id = source.id,
                iconId = source.iconId,
                time = uiDateMapper.map(source.date),
                temperature = source.temperatureMax,
                weatherBackgroundModel = WeatherBackgroundModel(source.iconId, isDay = WeatherModelMapper.isDay(source.iconId), hourOfDay = DateBuilder(source.date, source.timezone).getHour24Format(),
                        rain = source.rain, clouds = source.cloudiness, snow = source.snow)
        )
    }


}