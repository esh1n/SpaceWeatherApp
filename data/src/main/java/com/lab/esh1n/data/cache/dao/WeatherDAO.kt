package com.lab.esh1n.data.cache.dao

import androidx.room.*
import com.lab.esh1n.data.cache.DateConverter
import com.lab.esh1n.data.cache.contract.WeatherTableContract
import com.lab.esh1n.data.cache.entity.WeatherEntry
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import io.reactivex.Completable
import io.reactivex.Flowable
import java.util.*

/**
 * Created by esh1n on 3/9/18.
 */

@Dao
@TypeConverters(DateConverter::class)
interface WeatherDAO {

    @Query("SELECT * FROM weather INNER JOIN  place ON place.id = placeId WHERE isCurrent = 1 AND measured_at>=:almostNow AND measured_at<:plus5days  ORDER BY abs(:almostNow - measured_at) ASC")
    fun getDetailedCurrentWeather(almostNow: Date, plus5days: Date): Flowable<List<WeatherWithPlace>>

    @Query("SELECT DISTINCT * FROM weather INNER JOIN  place ON place.id = placeId WHERE isCurrent = 1 ORDER BY abs(:now - measured_at) ASC")
    fun getCurrentWeather(now: Date): Flowable<WeatherWithPlace>


    @Query("DELETE FROM " + WeatherTableContract.WEATHER_TABLE_NAME)
    fun clear()

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveWeather(entities: WeatherEntry): Completable

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveWeathers(entities: List<WeatherEntry>): Completable

}