package com.lab.esh1n.weather.domain.prefs

import com.lab.esh1n.weather.data.cache.LanguageTag

data class UserSettings(
    val notificationEnabled: Boolean,
    val autoPlaceDetection: Boolean,
    val alertsMailing: Boolean,
    val language: LanguageTag
)