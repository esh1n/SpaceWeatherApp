package com.lab.esh1n.weather.domain.weather.places.mapper

import com.esh1n.core_android.map.ListMapper
import com.lab.esh1n.data.api.response.PlaceAsset
import com.lab.esh1n.data.cache.entity.Coordinate
import com.lab.esh1n.data.cache.entity.PlaceEntry
import java.util.*

class PlaceEntryMapper : ListMapper<PlaceAsset, PlaceEntry>() {
    override fun map(source: PlaceAsset): PlaceEntry {
        val defaultTimeZone = "Europe/Moscow"
        val now = Date().time
        val name = source.name ?: ""
        return PlaceEntry(id = source.id,
                placeName = name,
                timezone = defaultTimeZone,
                coordinate = Coordinate(source.coord?.lat ?: 0.0, source.coord?.lon ?: 0.0),
                sunrise = now,
                sunset = now,
                isLiked = isLiked(name),
                isCurrent = isCurrent(name))
    }

    private fun isLiked(placeName: String): Boolean {
        val likedPlaces = listOf("Saint Petersburg", "Olomoucký kraj", "Venezia", "Moscow", "İstanbul", "Valencia")
        return likedPlaces.contains(placeName)
    }

    private fun isCurrent(placeName: String): Boolean {
        return placeName == "Voronezh"
    }
}