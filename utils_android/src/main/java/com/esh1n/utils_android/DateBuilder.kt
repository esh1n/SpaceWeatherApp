package com.esh1n.utils_android

import java.util.*

class DateBuilder @JvmOverloads constructor(initDate: Date = Date(), timeZoneID: String = "UTC") {
    private var calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZoneID))

    init {
        calendar.time = initDate
    }


    fun build(): Date {
        return calendar.time
    }

    fun getDay(): Int {
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    fun getHour24Format(): Int {
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    fun withDayInMonth(dayInMonth: Int): DateBuilder {
        calendar.set(Calendar.DAY_OF_MONTH, dayInMonth)
        return this
    }

    fun resetToDayStart(): DateBuilder {
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        return this
    }

    fun resetToDayEnd(): DateBuilder {
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        return this
    }

    fun endOfNight(): DateBuilder {
        calendar.set(Calendar.HOUR_OF_DAY, 4)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        return this
    }

    fun withHourOfDay(hourOfDay: Int): DateBuilder {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.SECOND, 0)
        return this
    }

    fun withMinute(minute: Int): DateBuilder {
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        return this
    }

    fun withMidnight(): DateBuilder {
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        return this
    }

    fun withMonth(month: Int): DateBuilder {
        calendar.set(Calendar.MONTH, month)
        return this
    }

    fun minusMinutes(minutes: Int): DateBuilder {
        calendar.add(Calendar.MINUTE, -minutes)
        return this
    }

    fun minusHours(hours: Int): DateBuilder {
        calendar.add(Calendar.HOUR_OF_DAY, -hours)
        return this
    }

    fun plusDays(days: Int): DateBuilder {
        calendar.add(Calendar.DAY_OF_MONTH, days)
        return this
    }

    fun withFirstMonth(): DateBuilder {
        calendar.set(Calendar.MONTH, 0)
        return this
    }

    fun withYear(year: Int): DateBuilder {
        calendar.set(Calendar.YEAR, year)
        return this
    }

    fun withLastMonth(): DateBuilder {
        calendar.set(Calendar.MONTH, calendar.getActualMaximum(Calendar.MONTH))
        return this
    }

    fun withLastDayInMonth(): DateBuilder {
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        return this
    }

    fun withFirstDayInMonth(): DateBuilder {
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        return this
    }

    fun nextYear(): DateBuilder {
        calendar.add(Calendar.YEAR, 1)
        return this
    }

    fun plusMinute(): DateBuilder {
        calendar.add(Calendar.MINUTE, 1)
        return this
    }

    fun setMinute(minute: Int): DateBuilder {
        calendar.set(Calendar.MINUTE, minute)
        return this
    }

    fun setHour(hourOfDay: Int): DateBuilder {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        return this
    }

    fun previousDay(): DateBuilder {
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        return this
    }

    fun previousMonth(): DateBuilder {
        calendar.add(Calendar.MONTH, -1)
        return this
    }

    fun nextDay(): DateBuilder {
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        return this
    }

    fun nextMonth(): DateBuilder {
        calendar.add(Calendar.MONTH, 1)
        return this
    }

    fun getDayOfYear(): Int {
        return calendar.get(Calendar.DAY_OF_YEAR) ?: 0
    }

    fun getYear(): Int {
        return calendar.get(Calendar.YEAR) ?: 0
    }

    fun isSameDay(date: Date): Boolean {
        val dateBuilder = DateBuilder(date)
        return getDayOfYear() == dateBuilder.getDayOfYear() && getYear() == dateBuilder.getYear()
    }

    fun isSameDay(dayOfTheYear: Int): Boolean {
        return getDayOfYear() == dayOfTheYear
    }
}