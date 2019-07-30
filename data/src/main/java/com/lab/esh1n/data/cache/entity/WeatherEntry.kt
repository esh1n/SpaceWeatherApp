package com.lab.esh1n.data.cache.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import com.lab.esh1n.data.cache.contract.WeatherTableContract
import com.lab.esh1n.data.cache.contract.WeatherTableContract.Companion.COLUMN_WEATHER_DATE
import com.lab.esh1n.data.cache.contract.WeatherTableContract.Companion.COLUMN_WEATHER_PLACE_ID
import java.util.*


@Entity(tableName = WeatherTableContract.WEATHER_TABLE_NAME,
        primaryKeys = [COLUMN_WEATHER_DATE, COLUMN_WEATHER_PLACE_ID],
        foreignKeys = [ForeignKey(entity = PlaceEntry::class,
                parentColumns = arrayOf(WeatherTableContract.COLUMN_PLACE_ID),
                childColumns = arrayOf(COLUMN_WEATHER_PLACE_ID), onDelete = CASCADE)])

open class WeatherEntry(

        @ColumnInfo(name = COLUMN_WEATHER_PLACE_ID)
        var placeId: Int,

        @ColumnInfo(name = COLUMN_WEATHER_DATE)
        var date: Date,

        var temperature: Double,

        var temperatureMin: Double,

        var temperatureMax: Double,

        var iconId: String,

        var description: String,

        var windSpeed: Float,

        var windDegree: Int,

        var pressure: Int,

        var humidity: Int
)

