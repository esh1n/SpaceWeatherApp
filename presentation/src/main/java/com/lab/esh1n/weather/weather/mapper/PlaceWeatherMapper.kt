package com.lab.esh1n.weather.weather.mapper

import com.esh1n.core_android.map.Mapper
import com.lab.esh1n.data.cache.entity.PlaceWithCurrentWeatherEntry
import com.lab.esh1n.weather.weather.model.PlaceModel

class PlaceWeatherMapper : Mapper<PlaceWithCurrentWeatherEntry, PlaceModel>() {
    override fun map(source: PlaceWithCurrentWeatherEntry): PlaceModel {
        val uiDateMapper = UiDateMapper(source.timezone, UILocalizer.getDateFormat(DateFormat.FULL))
        return PlaceModel(
                name = source.placeName,
                id = source.id,
                iconId = source.iconId,
                time = uiDateMapper.map(source.date),
                temperature = source.temperatureMax
        )
    }
}