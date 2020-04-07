package com.lab.esh1n.weather.weather.mapper

import com.esh1n.core_android.map.TwoWayListMapper
import java.text.SimpleDateFormat
import java.util.*

class UiDateListMapper(private val format: SimpleDateFormat) : TwoWayListMapper<Date, String>() {

    override fun map(source: Date): String {
        return format.format(source)
    }

    override fun mapInverse(source: String): Date {
        return format.parse(source) ?: Date()
    }

}