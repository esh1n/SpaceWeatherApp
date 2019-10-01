package com.lab.esh1n.data.cache.dao

import androidx.room.*
import com.lab.esh1n.data.cache.DateConverter
import com.lab.esh1n.data.cache.contract.WeatherTableContract
import com.lab.esh1n.data.cache.entity.WeatherEntry
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

/**
 * Created by esh1n on 3/9/18.
 */

@Dao
@TypeConverters(DateConverter::class)
abstract class WeatherDAO {

    @Query("SELECT * FROM weather INNER JOIN  place ON place.id = placeId WHERE isCurrent = 1 AND measured_at>=:almostNow AND measured_at<:plus5days  ORDER BY abs(:almostNow - measured_at) ASC")
    abstract fun getDetailedCurrentWeather(almostNow: Date, plus5days: Date): Flowable<List<WeatherWithPlace>>

    @Query("SELECT DISTINCT * FROM weather INNER JOIN  place ON place.id = placeId WHERE isCurrent = 1 ORDER BY abs(:now - measured_at) ASC")
    abstract fun getCurrentWeather(now: Date): Flowable<WeatherWithPlace>


    @Query("DELETE FROM " + WeatherTableContract.WEATHER_TABLE_NAME)
    abstract fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveWeather(entities: WeatherEntry): Completable

    @Transaction
    open fun updateCurrentWeather(weather: WeatherEntry) {
        val dateInMills = weather.date.time
        deletePreviousEntries(dateInMills, weather.placeId)
        saveWeather(weather)
    }

    @Query("DELETE FROM weather WHERE placeId=:cityId AND measured_at < :newCurrentDate ")
    abstract fun deletePreviousEntries(newCurrentDate: Long, cityId: Int)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveWeathers(entities: List<WeatherEntry>): Completable

    @Query("SELECT EXISTS(SELECT 1 FROM weather WHERE placeId=:id AND measured_at>:fourDaysAfterNow)")
    abstract fun checkIf4daysForecastExist(id: Int, fourDaysAfterNow: Date): Single<Int>

    @Query("DELETE FROM weather WHERE measured_at<=:threeHoursAgo")
    abstract fun clearOldWeathers(threeHoursAgo: Date): Completable

}