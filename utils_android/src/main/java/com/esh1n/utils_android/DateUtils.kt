package com.esh1n.utils_android

import java.util.*
import java.util.concurrent.TimeUnit

object DateUtils {
    fun getNow(timeUnit: TimeUnit): Long {
        return timeUnit.convert(Date().time, TimeUnit.MILLISECONDS)
    }

    fun getTimezoneByOffset(secondsOffset: Int, fallbackZone: String = "Europe/Moscow"): String {
        val offset = TimeUnit.MILLISECONDS.convert(secondsOffset.toLong(), TimeUnit.SECONDS)
        val timezones = TimeZone.getAvailableIDs(offset.toInt())
        return if (timezones.isNullOrEmpty()) fallbackZone else {
            val first = timezones[0]
            val etcZone = timezones.find { it.startsWith("Etc") }
            return etcZone ?: first
        }
    }
}