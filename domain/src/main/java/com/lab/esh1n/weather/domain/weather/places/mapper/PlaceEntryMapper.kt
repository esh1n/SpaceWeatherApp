package com.lab.esh1n.weather.domain.weather.places.mapper

import com.lab.esh1n.data.api.response.CoordResponse
import com.lab.esh1n.data.api.response.PlaceAsset
import com.lab.esh1n.data.cache.entity.Coordinate
import com.lab.esh1n.data.cache.entity.PlaceEntry
import java.util.*
import kotlin.collections.ArrayList

class PlaceEntryMapper {
    fun mapItem(source: PlaceAsset): PlaceEntry {
        val defaultTimeZone = "Europe/Moscow"
        val now = Date().time
        val name = source.name ?: ""
        val isLiked = isLiked(source.id)
        return PlaceEntry(id = source.id,
                placeName = name,
                timezone = defaultTimeZone,
                coordinate = mapCoordinate(source.coord),
                sunrise = now,
                sunset = now,
                isLiked = isLiked,
                isCurrent = isCurrent(source.id))
    }

    fun mapCoordinate(coord: CoordResponse?): Coordinate {
        return if (coord == null) {
            Coordinate(0.0, 0.0)
        } else {
            Coordinate(coord.lat ?: 0.0, coord.lon ?: 0.0)
        }
    }

    fun map(source: List<PlaceAsset>): List<PlaceEntry> {
        val entries = ArrayList<PlaceEntry>()
        source.forEach {
            if (!it.name.isNullOrBlank() && it.name != "-") {
                entries.add(mapItem(it))
            }
        }
        return entries
    }

    private fun isLiked(placeId: Int): Boolean {
        val likedPlaces = listOf(498817, 3339542,
                3164603, 524894, 745042, 6362115)
        return likedPlaces.contains(placeId)
    }

    private fun isCurrent(placeId: Int): Boolean {
        return placeId == 472045
    }
}