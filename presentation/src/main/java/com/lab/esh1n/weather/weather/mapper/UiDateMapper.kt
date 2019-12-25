package com.lab.esh1n.weather.weather.mapper

import com.esh1n.core_android.map.TwoWayMapper
import java.text.SimpleDateFormat
import java.util.*

class UiDateMapper(private val format: SimpleDateFormat) : TwoWayMapper<Date, String>() {

    override fun map(source: Date): String {
        return format.format(source)
    }

    override fun mapInverse(source: String): Date {
        return format.parse(source)
    }

}