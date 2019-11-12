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

    @Query("SELECT * FROM weather INNER JOIN  place ON place.id = placeId WHERE isCurrent = 1 AND epochDateMills>=:almostNow AND epochDateMills<:plus5days  ORDER BY abs(:almostNow - epochDateMills) ASC")
    abstract fun getDetailedCurrentWeather(almostNow: Date, plus5days: Date): Flowable<List<WeatherWithPlace>>

    @Query("SELECT * FROM weather INNER JOIN  place ON place.id = placeId WHERE placeId =:placeId AND epochDateMills>=:almostNow ORDER BY abs(:almostNow - epochDateMills) ASC")
    abstract fun getAllWeathersForCity(placeId:Int,almostNow: Date): Single<List<WeatherWithPlace>>

    @Query("SELECT DISTINCT * FROM weather INNER JOIN  place ON place.id = placeId WHERE isCurrent = 1 ORDER BY abs(:now - epochDateMills) ASC")
    abstract fun getCurrentWeather(now: Date): Flowable<WeatherWithPlace>


    @Query("DELETE FROM " + WeatherTableContract.WEATHER_TABLE_NAME)
    abstract fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveWeatherCompletable(entities: WeatherEntry): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveWeather(entities: WeatherEntry)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveWeathersCompletable(entities: List<WeatherEntry>): Completable


    @Transaction
    open fun updateCurrentWeathers(weathers: List<WeatherEntry>) {
        weathers.forEach {
            saveCurrentAndDeleteOldWeather(it)
        }
    }

    open fun saveCurrentAndDeleteOldWeather(weather: WeatherEntry) {
        saveWeather(weather)
        val dateInMills = weather.date.time
        deletePreviousEntries(dateInMills, weather.placeId)

    }

    @Query("DELETE FROM weather WHERE placeId=:cityId AND epochDateMills<:newCurrentDate ")
    abstract fun deletePreviousEntries(newCurrentDate: Long, cityId: Int)


    @Query("SELECT EXISTS(SELECT 1 FROM weather WHERE placeId=:id AND epochDateMills>:fourDaysAfterNow)")
    abstract fun checkIf4daysForecastExist(id: Int, fourDaysAfterNow: Date): Single<Int>

    @Query("DELETE FROM weather WHERE epochDateMills<=:threeHoursAgo")
    abstract fun clearOldWeathers(threeHoursAgo: Date): Completable

}