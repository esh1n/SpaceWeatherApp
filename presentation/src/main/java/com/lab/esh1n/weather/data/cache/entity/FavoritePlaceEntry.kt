package com.lab.esh1n.weather.data.cache.entity

data class FavoritePlaceEntry(
    val id: Int,
    val name: String,
    val iconId: String,
    val description: String,
    val temperature: Temperature
)