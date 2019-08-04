package com.lab.esh1n.weather.domain.weather.weather.mapper


import com.esh1n.core_android.map.TwoWayMapper
import java.util.*
import java.util.concurrent.TimeUnit

class EpochDateMapper : TwoWayMapper<Long, Date>() {
    override fun mapInverse(source: Date): Long {
        return TimeUnit.SECONDS.convert(source.time, TimeUnit.MILLISECONDS)
    }

    override fun map(source: Long): Date {
        val milliSeconds = TimeUnit.MILLISECONDS.convert(source, TimeUnit.SECONDS)
        return Date(milliSeconds)
    }
}




