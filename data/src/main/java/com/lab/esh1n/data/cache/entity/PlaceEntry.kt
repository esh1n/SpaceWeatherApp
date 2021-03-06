package com.lab.esh1n.data.cache.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lab.esh1n.data.cache.contract.WeatherTableContract

@Entity(tableName = WeatherTableContract.PLACE_TABLE_NAME)
data class PlaceEntry(
        @PrimaryKey
        @ColumnInfo(name = WeatherTableContract.COLUMN_PLACE_ID)
        var id: Int,

        var placeName: String,

        var countryCode: String,

        var timezone: String,

        var isCurrent: Boolean,

        var isLiked: Boolean,

        var sunset: Long,

        var sunrise: Long,

        @Embedded
        val coordinate: Coordinate
)


