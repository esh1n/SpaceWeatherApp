package com.lab.esh1n.data.cache.entity

import androidx.room.ColumnInfo
import com.lab.esh1n.data.cache.contract.WeatherTableContract
import java.util.*

data class PlaceWithCurrentWeatherEntry(val id: Int,
                                        val placeName: String,
                                        val iconId: String,
                                        val timezone: String,
                                        val dateTxt: String,
                                        @ColumnInfo(name = WeatherTableContract.COLUMN_WEATHER_DATE)
                                        var date: Date,
                                        val temperatureMax: Int) {

}