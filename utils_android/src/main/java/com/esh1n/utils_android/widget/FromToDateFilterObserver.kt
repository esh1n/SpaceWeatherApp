package com.esh1n.utils_android.widget

import java.util.Date

interface FromToDateFilterObserver {
    fun onDateFilterChanged(fromDate: Date, toDate: Date)
}
