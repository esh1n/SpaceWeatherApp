package com.lab.esh1n.data.cache.dao

import androidx.room.*
import com.lab.esh1n.data.cache.DateConverter
import com.lab.esh1n.data.cache.contract.WeatherTableContract
import com.lab.esh1n.data.cache.entity.WeatherEntry
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Created by esh1n on 3/9/18.
 */

@Dao
@TypeConverters(DateConverter::class)
interface WeatherDAO {

    @Query("SELECT DISTINCT * FROM weather INNER JOIN  place ON place.id = placeId WHERE placeName =:cityName ORDER BY measured_at DESC")
    fun getWeather(cityName: String): Flowable<WeatherWithPlace>

    @Query("DELETE FROM " + WeatherTableContract.WEATHER_TABLE_NAME)
    fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveWeather(entities: WeatherEntry): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveWeathers(entities: List<WeatherEntry>)
}