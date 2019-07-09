package com.lab.esh1n.weather.domain.weather.mapper

import com.lab.esh1n.weather.domain.base.Mapper

import java.util.*
import java.util.concurrent.TimeUnit

class EpochDateMapper : Mapper<Long, Date>() {
    override fun map(source: Long): Date {
        val millSeconds = TimeUnit.MILLISECONDS.convert(source, TimeUnit.SECONDS)
        return Date(millSeconds)
    }
}




