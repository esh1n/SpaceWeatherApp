package com.lab.esh1n.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lab.esh1n.data.cache.dao.WeatherDAO
import com.lab.esh1n.data.cache.entity.WeatherEntity

/**
 * Created by esh1n on 3/7/18.
 */
@Database(entities = [WeatherEntity::class], version = 2, exportSchema = false)
@TypeConverters(DBTypeConverters::class)
abstract class WeatherDB : RoomDatabase() {

    abstract fun weatherDAO(): WeatherDAO

    companion object {
        const val NAME = "weather.db"
    }

}