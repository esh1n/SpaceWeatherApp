package com.lab.esh1n.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.TypeConverters
import com.lab.esh1n.data.cache.DateConverter
import com.lab.esh1n.data.cache.entity.PlaceEntry
import io.reactivex.Single

@Dao
@TypeConverters(DateConverter::class)
abstract class PlaceDAO {
    @Insert(onConflict = REPLACE)
    abstract fun insertPlaces(places: List<PlaceEntry>)

    @Query("SELECT id from place where placeName=:placeName")
    abstract fun getPlaceIdByName(placeName: String): Single<Int>
}