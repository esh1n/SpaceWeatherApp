package com.lab.esh1n.data.cache.dao

import androidx.paging.DataSource
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import com.lab.esh1n.data.cache.DateConverter
import com.lab.esh1n.data.cache.entity.PlaceEntry
import com.lab.esh1n.data.cache.entity.PlaceWithCurrentWeatherEntry
import com.lab.esh1n.data.cache.entity.SunsetSunrisePlaceEntry
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

@Dao
@TypeConverters(DateConverter::class)
abstract class PlaceDAO {
    @Insert(onConflict = IGNORE)
    abstract fun insertPlaces(places: List<PlaceEntry>): Completable

    @Query("SELECT id from place where placeName=:placeName")
    abstract fun getPlaceIdByName(placeName: String): Single<Int>

    @Query("SELECT id,placeName,iconId,temperatureMax,epochDateMills,timezone,dateTxt,rain,cloudiness,snow,description as weatherDescription  FROM place  LEFT JOIN weather w ON place.id = w.placeId AND w.epochDateMills = (SELECT epochDateMills FROM weather innerW WHERE  innerW.placeId = w.placeId ORDER BY abs(:now - epochDateMills) ASC LIMIT 1) ORDER BY isLiked DESC,placeName")
    abstract fun getAllPlacesWithCurrentWeather(now: Date): DataSource.Factory<Int, PlaceWithCurrentWeatherEntry>

    @Query("SELECT id from place WHERE isCurrent = 1")
    abstract fun getCurrentCityId(): Single<Int>

    @Query("UPDATE place SET isCurrent = 1 WHERE id = :id")
    abstract fun setCurrentPlace(id: Int)

    @Query("UPDATE place SET isCurrent = 0")
    abstract fun deselectCurrentPlace()

    @Query("UPDATE place SET sunrise = :sunrise,sunset = :sunset WHERE id =:id")
    abstract fun updateSunsetSunrise(id: Int, sunrise: Date, sunset: Date)

    @Query("SELECT id,sunrise,sunset from place WHERE isCurrent = 1")
    abstract fun getCurrentSunsetSunriseInfo(): Flowable<SunsetSunrisePlaceEntry>

    @Transaction
    open fun updateSunrisesAndSunset(entries: List<SunsetSunrisePlaceEntry>) {
        entries.forEach { entry ->
            run {
                updateSunsetSunrise(entry.id, entry.sunrise, entry.sunset)
            }
        }
    }

    @Query("SELECT id from place WHERE isLiked = 1 OR isCurrent = 1")
    abstract fun getPlaceIdsToSync(): Single<List<Int>>

    @Transaction
    open fun updateCurrentPlace(id: Int) {
        deselectCurrentPlace()
        setCurrentPlace(id)
    }

    @Query("SELECT EXISTS(SELECT 1 FROM place WHERE isCurrent = 1)")
    abstract fun checkIfCurrentPlaceExist(): Single<Boolean>

    @Query("SELECT id FROM place WHERE isCurrent = 1")
    abstract fun loadCurrentPlaceId(): Single<Int>

}