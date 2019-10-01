package com.lab.esh1n.data.cache.contract

/**
 * Created by esh1n on 3/9/18.
 */

interface WeatherTableContract {
    companion object {
        const val WEATHER_TABLE_NAME = "weather"
        const val PLACE_TABLE_NAME = "place"
        const val COLUMN_PLACE_ID = "id"
        const val COLUMN_WEATHER_PLACE_ID = "placeId"
        const val COLUMN_WEATHER_DATE = "epochDateMills"
        const val COLUMN_WEATHER_DATE_TXT = "dateTxt"
    }
}
