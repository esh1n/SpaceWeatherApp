package com.lab.esh1n.weather.weather.mapper

import com.esh1n.core_android.map.Mapper
import com.lab.esh1n.data.cache.entity.PlaceEntry
import com.lab.esh1n.weather.weather.model.PlaceWeather

class PlaceWeatherMapper : Mapper<PlaceEntry, PlaceWeather>() {
    override fun map(source: PlaceEntry): PlaceWeather {
        return PlaceWeather(
                placeName = source.placeName
        )
    }
}