package com.lab.esh1n.weather.weather.mapper

import java.text.SimpleDateFormat
import java.util.*

object UILocalizer {

    fun getDateFormat(dateFormat: DateFormat): SimpleDateFormat {
        val hardcodedLocale = Locale.US
        return dateFormat.getSimpleDateFormat(hardcodedLocale).get()!!
    }
}