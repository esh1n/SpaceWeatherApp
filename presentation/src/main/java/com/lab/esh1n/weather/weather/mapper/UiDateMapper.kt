package com.lab.esh1n.weather.weather.mapper

import com.esh1n.core_android.map.Mapper
import java.text.SimpleDateFormat
import java.util.*

class UiDateMapper(private val timezone: String) : Mapper<Date, String>() {

    override fun map(source: Date): String {
        val formatter = UI_FORMATTER.get()!!
        formatter.timeZone = TimeZone.getTimeZone(timezone)
        return formatter.format(source)
    }

    companion object {
        private val UI_FORMATTER = object : ThreadLocal<SimpleDateFormat>() {
            override fun initialValue(): SimpleDateFormat {
                val df = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
                df.timeZone = TimeZone.getTimeZone("UTC")
                return df
            }
        }
    }
}