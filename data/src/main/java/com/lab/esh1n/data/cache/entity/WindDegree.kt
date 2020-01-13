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

enum class WindDirection(val degree: Float) {
    NORTH(0f), NORTH_EAST(45f), EAST(90f), SOUTH_EAST(135f), SOUTH(180f), SOUTH_WEST(225f), WEST(270f), NORTH_WEST(315f), N_A(0f)
}