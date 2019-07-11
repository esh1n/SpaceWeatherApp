package com.lab.esh1n.data.cache

import androidx.room.TypeConverter
import java.util.*


/**
 * Created by esh1n on 3/9/18.
 */

class DBTypeConverters {

    @TypeConverter
    fun longToDate(dateLong: Long): Date {
        return Date(dateLong)
    }

    @TypeConverter
    fun dateToLong(date: Date): Long {
        return date.time
    }
}
