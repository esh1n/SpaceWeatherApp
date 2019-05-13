package com.lab.esh1n.data.cache.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lab.esh1n.data.cache.contract.WeatherTableContract
import java.util.*

@Entity(tableName = WeatherTableContract.TABLE_NAME)
data class WeatherEntity(

        @PrimaryKey
        @ColumnInfo(name = WeatherTableContract.COLUMN_ID)
        var id: Int,

        @ColumnInfo(name = WeatherTableContract.COLUMN_CITY)
        var cityName: String,

        @ColumnInfo(name = WeatherTableContract.COLUMN_TEMPERATURE)
        var temperature: Double,

        @ColumnInfo(name = WeatherTableContract.COLUMN_TEMPERATURE_MIN)
        var temperatureMin: Double,

        @ColumnInfo(name = WeatherTableContract.COLUMN_TEMPERATURE_MAX)
        var temperatureMax: Double,

        @ColumnInfo(name = WeatherTableContract.COLUMN_ICON_ID)
        var iconId: String,

        @ColumnInfo(name = WeatherTableContract.COLUMN_DESCRIPTION)
        var description: String,

        @ColumnInfo(name = WeatherTableContract.COLUMN_WIND_SPEED)
        var windSpeed: Int,

        @ColumnInfo(name = WeatherTableContract.COLUMN_WIND_DEGREE)
        var windDegree: Int,

        @ColumnInfo(name = WeatherTableContract.COLUMN_PRESSURE)
        var pressure: Int,

        @ColumnInfo(name = WeatherTableContract.COLUMN_HUMIDITY)
        var humidity: Int,

        @ColumnInfo(name = WeatherTableContract.COLUMN_DATE)
        var date: Date
)

data class WeatherModel(val id: Long,
                        val cityName: String,
                        val temperature: Int,
                        val iconUrl: String,
                        val tempMin: Float,
                        val tempMax: Float,
                        val description: String,
                        val windSpeed: Int,
                        val pressure: Int,
                        val humidity: Int)