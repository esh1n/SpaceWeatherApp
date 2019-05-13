package com.lab.esh1n.data.cache.contract

/**
 * Created by esh1n on 3/9/18.
 */

interface WeatherTableContract {
    companion object {
        const val TABLE_NAME = "weathers"
        const val COLUMN_ID = "id"
        const val COLUMN_CITY = "cityName"
        const val COLUMN_TEMPERATURE_MAX = "temp_max"
        const val COLUMN_TEMPERATURE_MIN = "temp_min"
        const val COLUMN_TEMPERATURE = "temp"
        const val COLUMN_ICON_ID = "icon_id"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_WIND_SPEED = "wind_speed"
        const val COLUMN_WIND_DEGREE = "wind_degree"
        const val COLUMN_HUMIDITY = "humidity"
        const val COLUMN_PRESSURE = "pressure"
        const val COLUMN_DATE = "measured_at"
    }
}
