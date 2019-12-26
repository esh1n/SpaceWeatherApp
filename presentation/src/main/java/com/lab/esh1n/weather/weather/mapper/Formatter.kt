package com.lab.esh1n.weather.weather.mapper

import java.text.SimpleDateFormat
import java.util.*


enum class DateFormat(val format: String) {
    FULL("yyyy-MM-dd HH:mm") {
        override fun getSimpleDateFormat(locale: Locale, timeZone: String): ThreadLocal<SimpleDateFormat> {
            return getDateFormat(this.format, locale, timeZone)
        }
    },
    HOUR("HH:mm") {
        override fun getSimpleDateFormat(locale: Locale, timeZone: String): ThreadLocal<SimpleDateFormat> {
            return getDateFormat(this.format, locale, timeZone)
        }
    },
    HOUR_DAY("HH:mm EEE") {
        override fun getSimpleDateFormat(locale: Locale, timeZone: String): ThreadLocal<SimpleDateFormat> {
            return getDateFormat(this.format, locale, timeZone)
        }
    },
    DAY_HOUR("HH:mm d MMM") {
        override fun getSimpleDateFormat(locale: Locale, timeZone: String): ThreadLocal<SimpleDateFormat> {
            return getDateFormat(this.format, locale, timeZone)
        }
    },
    DAY_OF_WEEK("EEEE") {
        override fun getSimpleDateFormat(locale: Locale, timeZone: String): ThreadLocal<SimpleDateFormat> {
            return getDateFormat(this.format, locale, timeZone)
        }
    },
    DAY_OF_WEEK_SHORT("EEE") {
        override fun getSimpleDateFormat(locale: Locale, timeZone: String): ThreadLocal<SimpleDateFormat> {
            return getDateFormat(this.format, locale, timeZone)
        }
    },
    MONTH_DAY("d MMMM") {
        override fun getSimpleDateFormat(locale: Locale, timeZone: String): ThreadLocal<SimpleDateFormat> {
            return getDateFormat(this.format, locale, timeZone)
        }
    };


    abstract fun getSimpleDateFormat(locale: Locale, timeZone: String = "UTC"): ThreadLocal<SimpleDateFormat>

    fun getDateFormat(format: String, locale: Locale, timeZone: String): ThreadLocal<SimpleDateFormat> {
        return object : ThreadLocal<SimpleDateFormat>() {
            override fun initialValue(): SimpleDateFormat {
                val df = SimpleDateFormat(format, locale)
                df.timeZone = TimeZone.getTimeZone(timeZone)
                return df
            }
        }
    }
}
