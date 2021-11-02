package com.lab.esh1n.weather.data.api.response

data class CityResponse(
        val country: String? = null,
        val coord: CoordResponse? = null,
        val timezone: Int? = null,
        val name: String? = null,
        val id: Int? = null,
        val sunrise: Long? = null,
        val sunset: Long? = null
)
