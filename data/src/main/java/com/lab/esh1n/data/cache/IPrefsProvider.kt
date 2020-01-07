package com.lab.esh1n.data.cache

import java.util.*

interface IPrefsProvider {
    fun getUnits(): Units
    fun getLocale(): Locale
    fun getTemperatureUnits(): TemperatureUnit
}

enum class Units(val serverValue: String) {
    METRIC("metric"), IMPERIAL("imperial");
}

enum class TemperatureUnit {
    F, C
}


