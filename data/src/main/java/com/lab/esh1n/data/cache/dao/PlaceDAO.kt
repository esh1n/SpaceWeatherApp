package com.lab.esh1n.data.cache.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.lab.esh1n.data.cache.DateConverter
import com.lab.esh1n.data.cache.entity.PlaceEntry
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
@TypeConverters(DateConverter::class)
abstract class PlaceDAO {
    @Insert(onConflict = REPLACE)
    abstract fun insertPlaces(places: List<PlaceEntry>)

    @Query("SELECT id from place where placeName=:placeName")
    abstract fun getPlaceIdByName(placeName: String): Single<Int>

    @Query("SELECT * from place")
    abstract fun getAllPlaces(): Flowable<List<PlaceEntry>>

    @Query("SELECT id from place WHERE isCurrent = 1")
    abstract fun getCurrentCityId(): Single<Int>

    @Query("UPDATE place SET isCurrent = 1 WHERE id = :id")
    abstract fun setCurrentPlace(id: Int)

    @Query("UPDATE place SET isCurrent = 0")
    abstract fun deselectCurrentPlace()


    @Transaction
    open fun updateCurrentPlace(id: Int) {
        deselectCurrentPlace()
        setCurrentPlace(id)
    }

}