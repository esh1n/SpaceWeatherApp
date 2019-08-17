package com.lab.esh1n.weather.weather.mapper

import com.esh1n.core_android.map.Mapper
import java.text.SimpleDateFormat
import java.util.*

class UiDateMapper(private val timezone: String, private val format: SimpleDateFormat) : Mapper<Date, String>() {

    override fun map(source: Date): String {
        format.timeZone = TimeZone.getTimeZone(timezone)
        return format.format(source)
    }

}