package com.lab.esh1n.weather.domain.places.mapper

import com.lab.esh1n.weather.data.api.response.CoordResponse
import com.lab.esh1n.weather.data.api.response.PlaceAsset
import com.lab.esh1n.weather.data.cache.entity.Coordinate
import com.lab.esh1n.weather.data.cache.entity.PlaceEntry
import java.util.*
import kotlin.collections.ArrayList

class PlaceEntryMapper {
    private fun mapItem(source: PlaceAsset): PlaceEntry {
        val defaultTimeZone = "Europe/Moscow"
        val now = Date().time
        val isLiked = isLiked(source.id)
        return PlaceEntry(id = source.id,
                placeName = source.name ?: "",
                timezone = defaultTimeZone,
                coordinate = mapCoordinate(source.coord),
                sunrise = now,
                sunset = now,
                isLiked = isLiked,
                countryCode = source.country ?: "US",
                isCurrent = isCurrent(source.id))
    }

    private fun mapCoordinate(coord: CoordResponse?): Coordinate {
        return if (coord == null) {
            Coordinate(0.0, 0.0)
        } else {
            Coordinate(coord.lat ?: 0.0, coord.lon ?: 0.0)
        }
    }

    fun map(source: List<PlaceAsset>, emitter: ((Float) -> Unit), stepsCount: Int = 5): List<PlaceEntry> {
        val RESULT = ArrayList<PlaceEntry>()
        if (source.isNotEmpty()) {
            val step = source.size / stepsCount
            val stepList = arrayListOf<Int>()
            for (x in source.indices step step) {
                stepList.add(x)
            }
            stepList.add(source.size - 1)
            val realSteps = stepList.size
            var stepIndex = 0
            source.forEachIndexed { index, value ->
                if (!value.name.isNullOrBlank() && value.name != "-") {
                    RESULT.add(mapItem(value))
                }
                if (stepIndex < realSteps && index == stepList[stepIndex]) {
                    val currentStepValue = stepList[stepIndex]
                    stepIndex++
                    val relation = currentStepValue.toFloat() / source.size
                    emitter(relation)
                }

            }
        }
        return RESULT
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