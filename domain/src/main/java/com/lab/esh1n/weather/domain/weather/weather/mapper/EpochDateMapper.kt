package com.lab.esh1n.weather.domain.weather.weather.mapper


import com.esh1n.core_android.map.Mapper
import java.util.*
import java.util.concurrent.TimeUnit

class EpochDateMapper : Mapper<Long, Date>() {
    override fun map(source: Long): Date {
        val milliSeconds = TimeUnit.MILLISECONDS.convert(source, TimeUnit.SECONDS)
        return Date(milliSeconds)
    }
}




