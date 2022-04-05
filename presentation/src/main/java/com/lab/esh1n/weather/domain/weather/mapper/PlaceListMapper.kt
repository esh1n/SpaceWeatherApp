package com.lab.esh1n.weather.domain.weather.mapper

import com.esh1n.core_android.map.ListMapper
import com.esh1n.utils_android.DateUtils
import com.lab.esh1n.weather.data.api.response.CityResponse
import com.lab.esh1n.weather.data.cache.entity.SunsetSunriseTimezonePlaceEntry

class PlaceListMapper : ListMapper<CityResponse, SunsetSunriseTimezonePlaceEntry>() {
    private val dateMapper = EpochDateListMapper()
    override fun map(source: CityResponse): SunsetSunriseTimezonePlaceEntry {
        val timeZone = DateUtils.getTimezoneByOffset(source.timezone ?: 0)
        return SunsetSunriseTimezonePlaceEntry(
            id = source.id,
            sunrise = dateMapper.map(source.sunrise),
            sunset = dateMapper.map(source.sunset),
            timezone = timeZone
        )
    }
}