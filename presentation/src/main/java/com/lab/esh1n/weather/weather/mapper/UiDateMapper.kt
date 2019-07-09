package com.lab.esh1n.weather.weather.mapper

import com.lab.esh1n.weather.domain.base.Mapper
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class UiDateMapper : Mapper<Long, String>() {

    override fun map(source: Long): String {
        val millSeconds = TimeUnit.MILLISECONDS.convert(source, TimeUnit.SECONDS)
        return UI_FORMATTER.get()!!.format(Date(millSeconds))
    }

    companion object {
        private val UI_FORMATTER = object : ThreadLocal<SimpleDateFormat>() {
            override fun initialValue(): SimpleDateFormat {
                val df = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
                df.timeZone = TimeZone.getDefault()
                return df
            }
        }
    }
}