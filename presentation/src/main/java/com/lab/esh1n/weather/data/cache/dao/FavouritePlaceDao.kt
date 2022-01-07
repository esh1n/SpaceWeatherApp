package com.lab.esh1n.weather.data.cache.dao

import androidx.room.Dao
import androidx.room.Query
import com.lab.esh1n.weather.data.cache.entity.FavoritePlaceEntry
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
abstract class FavouritePlaceDao {

    @Query("SELECT isLiked from place WHERE id=:id")
    abstract fun getIsPlaceFavourite(id: Int): Flow<Boolean>

    @Query("SELECT id,placeName as name,iconId,temperatureMax as temperature,description  FROM place JOIN weather w ON place.id = w.placeId AND w.epochDateMills = (SELECT epochDateMills FROM weather innerW WHERE  innerW.placeId = w.placeId ORDER BY abs(:now - epochDateMills) ASC LIMIT 1) WHERE isLiked = 1 ORDER BY placeName")
    abstract fun loadFavouritesPlaces(now: Date): Flow<List<FavoritePlaceEntry>>
}