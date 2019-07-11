package com.esh1n.utils_android.widget

import java.util.Date

interface FromToDateObservable {
    fun currentFromDate(): Date?

    fun currentToDate(): Date?

    fun registerDateChangesObserver(o: FromToDateFilterObserver)

    fun removeObserver()

    fun notifyObserverDateChanged()

    fun refreshByMinMaxDates(minDate: Date, maxDate: Date)
}

