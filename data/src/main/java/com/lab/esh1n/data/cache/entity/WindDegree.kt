package com.lab.esh1n.data.cache.entity

import kotlin.math.roundToInt

class WindDegree(val degree: Double) {
    val direction: WindDirection = initWindDirection(degree)
    private fun initWindDirection(degree: Double): WindDirection {
        val directions = WindDirection.values()
        val position: Int = ((degree % 360) / 45).roundToInt()
        return directions[position % 8]
    }
}

enum class WindDirection {
    NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST, N_A
}