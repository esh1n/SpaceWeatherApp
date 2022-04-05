package com.lab.esh1n.weather.data.api.response

data class ForecastResponse(
    val city: CityResponse,
    val cnt: Int? = null,
    val cod: String? = null,
    val message: Double? = null,
    val list: List<ForecastItemResponse?>? = null
)
