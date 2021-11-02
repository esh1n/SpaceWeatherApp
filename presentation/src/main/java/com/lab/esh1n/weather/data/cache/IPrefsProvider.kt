package com.lab.esh1n.weather.data.cache

import java.util.*

interface IPrefsProvider {
    fun getAppUnits(): Units
    fun getServerAPIUnits(): Units
    fun getLocale(): Locale
    fun getAppTemperatureUnits(): TemperatureUnit
    fun getServerAPITemperatureUnits(): TemperatureUnit
}

enum class Units(val serverValue: String) {
    METRIC("metric"), IMPERIAL("imperial");
}

enum class TemperatureUnit {
    F, C;

    companion object {
        fun getBySystem(units: Units): TemperatureUnit {
            return if (units == Units.METRIC) {
                C
            } else {
                F
            }
        }
    }
}


