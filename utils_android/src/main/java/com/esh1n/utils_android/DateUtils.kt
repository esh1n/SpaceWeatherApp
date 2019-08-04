package com.esh1n.utils_android

import java.util.*
import java.util.concurrent.TimeUnit

object DateUtils {
    fun getNow(timeUnit: TimeUnit): Long {
        return timeUnit.convert(Date().time, TimeUnit.MILLISECONDS)
    }
}