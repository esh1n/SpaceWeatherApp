package com.lab.esh1n.data.cache.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lab.esh1n.data.cache.contract.WeatherTableContract
import com.lab.esh1n.data.cache.entity.WeatherEntity

/**
 * Created by esh1n on 3/9/18.
 */
@Dao
interface WeatherDAO {

    @Query("SELECT DISTINCT * FROM weathers WHERE cityName =:cityName ORDER BY measured_at DESC")
    fun getLiveWeather(cityName: String): LiveData<WeatherEntity>

    @Query("SELECT DISTINCT * FROM weathers WHERE cityName =:cityName ORDER BY measured_at DESC")
    suspend fun getWeather(cityName: String): WeatherEntity

    @Query("DELETE FROM " + WeatherTableContract.TABLE_NAME)
    fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveWeather(entities: WeatherEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveWeathers(entities: List<WeatherEntity>)
}