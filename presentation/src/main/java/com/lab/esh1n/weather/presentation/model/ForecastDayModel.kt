package com.lab.esh1n.weather.presentation.model

import java.io.Serializable
import java.util.*

data class ForecastDayModel(
    val dayDescription: String,
    val dayDate: Date,
    val placeId: Int,
    val timezone: String
) : Serializable