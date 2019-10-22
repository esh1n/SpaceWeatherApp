package com.lab.esh1n.weather.domain.weather.weather.mapper

import com.esh1n.core_android.map.Mapper
import com.lab.esh1n.data.api.response.CityResponse
import com.lab.esh1n.data.cache.entity.UpdatePlaceEntry
import java.util.*

class PlaceMapper(private val dateMapper: EpochDateMapper) : Mapper<CityResponse, UpdatePlaceEntry>() {
    override fun map(source: CityResponse): UpdatePlaceEntry {
        val now = Date().time
        return UpdatePlaceEntry(id = source.id!!,
                sunrise = dateMapper.map(source.sunrise ?: now),
                sunset = dateMapper.map(source.sunset ?: now))
    }
}