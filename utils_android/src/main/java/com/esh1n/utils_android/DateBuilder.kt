package com.esh1n.utils_android

import java.util.*

class DateBuilder {
    private var calendar: Calendar? = null

    constructor(initDate: Date) {
        calendar = Calendar.getInstance()
        calendar!!.time = initDate
    }

    constructor() {
        calendar = Calendar.getInstance()
    }

    constructor(year: Int, month: Int, dayOfMonth: Int) {
        calendar = Calendar.getInstance()
        calendar!!.set(Calendar.YEAR, year)
        calendar!!.set(Calendar.MONTH, month)
        calendar!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)
    }

    fun build(): Date {
        return calendar!!.time
    }

    fun withDayInMonth(dayInMonth: Int): DateBuilder {
        calendar!!.set(Calendar.DAY_OF_MONTH, dayInMonth)
        return this
    }

    fun withMorning(): DateBuilder {
        calendar!!.set(Calendar.HOUR_OF_DAY, 0)
        calendar!!.set(Calendar.MINUTE, 0)
        calendar!!.set(Calendar.SECOND, 0)
        return this
    }

    fun withHourOfDay(hourOfDay: Int): DateBuilder {
        calendar!!.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar!!.set(Calendar.SECOND, 0)
        return this
    }

    fun withMinute(minute: Int): DateBuilder {
        calendar!!.set(Calendar.MINUTE, minute)
        calendar!!.set(Calendar.SECOND, 0)
        return this
    }

    fun withMidnight(): DateBuilder {
        calendar!!.set(Calendar.HOUR_OF_DAY, 23)
        calendar!!.set(Calendar.MINUTE, 59)
        calendar!!.set(Calendar.SECOND, 59)
        return this
    }

    fun withMonth(month: Int): DateBuilder {
        calendar!!.set(Calendar.MONTH, month)
        return this
    }

    fun withFirstMonth(): DateBuilder {
        calendar!!.set(Calendar.MONTH, 0)
        return this
    }

    fun withYear(year: Int): DateBuilder {
        calendar!!.set(Calendar.YEAR, year)
        return this
    }

    fun withLastMonth(): DateBuilder {
        calendar!!.set(Calendar.MONTH, calendar!!.getActualMaximum(Calendar.MONTH))
        return this
    }

    fun withLastDayInMonth(): DateBuilder {
        calendar!!.set(Calendar.DAY_OF_MONTH, calendar!!.getActualMaximum(Calendar.DAY_OF_MONTH))
        return this
    }

    fun withFirstDayInMonth(): DateBuilder {
        calendar!!.set(Calendar.DAY_OF_MONTH, 1)
        return this
    }

    fun nextYear(): DateBuilder {
        calendar!!.add(Calendar.YEAR, 1)
        return this
    }

    fun plusMinute(): DateBuilder {
        calendar!!.add(Calendar.MINUTE, 1)
        return this
    }

    fun previousDay(): DateBuilder {
        calendar!!.add(Calendar.DAY_OF_MONTH, -1)
        return this
    }

    fun previousMonth(): DateBuilder {
        calendar!!.add(Calendar.MONTH, -1)
        return this
    }

    fun nextDay(): DateBuilder {
        calendar!!.add(Calendar.DAY_OF_MONTH, 1)
        return this
    }

    fun nextMonth(): DateBuilder {
        calendar!!.add(Calendar.MONTH, 1)
        return this
    }
}