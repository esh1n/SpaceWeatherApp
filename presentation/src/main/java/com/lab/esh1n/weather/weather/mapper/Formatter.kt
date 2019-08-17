package com.lab.esh1n.weather.weather.mapper

import java.text.SimpleDateFormat
import java.util.*


enum class DateFormat(val format: String) {
    FULL("yyyy-MM-dd HH:mm") {
        override fun getSimpleDateFormat(locale: Locale): ThreadLocal<SimpleDateFormat> {
            return getDateFormat(this.format, locale)
        }
    },
    HOUR("HH:mm") {
        override fun getSimpleDateFormat(locale: Locale): ThreadLocal<SimpleDateFormat> {
            return getDateFormat(this.format, locale)
        }
    },
    MONTH_DAY("d MMM") {
        override fun getSimpleDateFormat(locale: Locale): ThreadLocal<SimpleDateFormat> {
            return getDateFormat(this.format, locale)
        }
    };


    abstract fun getSimpleDateFormat(locale: Locale): ThreadLocal<SimpleDateFormat>

    fun getDateFormat(format: String, locale: Locale): ThreadLocal<SimpleDateFormat> {
        return object : ThreadLocal<SimpleDateFormat>() {
            override fun initialValue(): SimpleDateFormat {
                val df = SimpleDateFormat(format, locale)
                df.timeZone = TimeZone.getTimeZone("UTC")
                return df
            }
        }
    }
}