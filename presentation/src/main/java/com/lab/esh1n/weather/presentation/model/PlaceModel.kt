package com.lab.esh1n.weather.presentation.model

import androidx.annotation.DrawableRes

data class PlaceModel(
    val id: Int,
    val name: String,
    @DrawableRes
    val countryFlag: Int,
    val weatherDescription: String,
    val iconId: String,
    val time: String,
    val temperature: Int? = null,
    val weatherBackgroundModel: WeatherBackgroundModel
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlaceModel

        if (id != other.id) return false
        if (name != other.name) return false
        if (countryFlag != other.countryFlag) return false
        if (iconId != other.iconId) return false
        if (time != other.time) return false
        if (temperature != other.temperature) return false

        return true
    }

    override fun hashCode(): Int {
        val temp = temperature ?: 0
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + iconId.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + temp
        return result
    }
}