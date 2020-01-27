package com.lab.esh1n.weather.domain.weather.weather.mapper

import com.esh1n.core_android.map.ListMapper
import com.lab.esh1n.data.api.response.CityResponse
import com.lab.esh1n.data.cache.entity.SunsetSunrisePlaceEntry
import java.util.*

class PlaceListMapper : ListMapper<CityResponse, SunsetSunrisePlaceEntry>() {
    private val dateMapper = EpochDateListMapper()
    override fun map(source: CityResponse): SunsetSunrisePlaceEntry {

        val now = Date().time
        return SunsetSunrisePlaceEntry(id = source.id!!,
                sunrise = dateMapper.map(source.sunrise ?: now),
                sunset = dateMapper.map(source.sunset ?: now))
    }
}