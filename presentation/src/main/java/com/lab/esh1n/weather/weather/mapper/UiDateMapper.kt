package com.lab.esh1n.weather.weather.mapper

import com.lab.esh1n.weather.domain.base.Mapper
import java.text.SimpleDateFormat
import java.util.*

class UiDateMapper:Mapper<Date,String>() {

    override fun map(source: Date): String {
        return UI_FORMATTER.get()!!.format(source)
    }

    companion object {
        private val UI_FORMATTER = object : ThreadLocal<SimpleDateFormat>() {
            override fun initialValue(): SimpleDateFormat {
                val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                df.timeZone = TimeZone.getDefault()
                return df
            }
        }
    }
}