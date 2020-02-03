package com.lab.esh1n.weather.domain.weather.weather.mapper

import com.esh1n.core_android.map.ListMapper
import com.esh1n.utils_android.DateUtils
import com.lab.esh1n.data.api.response.CityResponse
import com.lab.esh1n.data.cache.entity.SunsetSunriseTimezonePlaceEntry
import java.util.*

class PlaceListMapper : ListMapper<CityResponse, SunsetSunriseTimezonePlaceEntry>() {
    private val dateMapper = EpochDateListMapper()
    override fun map(source: CityResponse): SunsetSunriseTimezonePlaceEntry {
        val now = Date().time
        val timeZone = DateUtils.getTimezoneByOffset(source.timezone ?: 0)
        return SunsetSunriseTimezonePlaceEntry(
                id = source.id!!,
                sunrise = dateMapper.map(source.sunrise ?: now),
                sunset = dateMapper.map(source.sunset ?: now),
                timezone = timeZone)
    }
}