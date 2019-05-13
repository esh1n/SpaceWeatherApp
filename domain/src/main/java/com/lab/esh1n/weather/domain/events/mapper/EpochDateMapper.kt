package com.lab.esh1n.weather.domain.events.mapper

import com.lab.esh1n.weather.domain.base.Mapper

import java.util.*
import java.util.concurrent.TimeUnit

class EpochDateMapper : Mapper<Long, Date>() {
    override fun map(source: Long): Date {
       return Date(TimeUnit.SECONDS.convert(source,TimeUnit.MILLISECONDS))
    }
}




