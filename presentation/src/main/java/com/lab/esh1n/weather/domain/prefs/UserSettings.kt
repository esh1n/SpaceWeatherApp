package com.lab.esh1n.weather.domain.prefs

import java.util.*

data class UserSettings(
    val notificationEnabled: Boolean,
    val autoPlaceDetection: Boolean,
    val alertsMailing: Boolean,
    val language: Locale
)