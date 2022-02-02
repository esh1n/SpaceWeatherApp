package com.lab.esh1n.weather.data.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lab.esh1n.weather.data.cache.dao.FavouritePlaceDao
import com.lab.esh1n.weather.data.cache.dao.PlaceDAO
import com.lab.esh1n.weather.data.cache.dao.WeatherDAO
import com.lab.esh1n.weather.data.cache.entity.PlaceEntry
import com.lab.esh1n.weather.data.cache.entity.WeatherEntry
import com.lab.esh1n.weather.data.converter.DateConverter
import com.lab.esh1n.weather.data.converter.TemperatureConverter
import com.lab.esh1n.weather.data.converter.WindDegreeConverter
import com.lab.esh1n.weather.data.converter.WindSpeedConverter
import java.util.concurrent.Executors

@Database(entities = [WeatherEntry::class, PlaceEntry::class], version = 2, exportSchema = false)
@TypeConverters(DateConverter::class, WindDegreeConverter::class, TemperatureConverter::class, WindSpeedConverter::class)
abstract class WeatherDB : RoomDatabase() {

    abstract fun weatherDAO(): WeatherDAO

    abstract fun placeDAO(): PlaceDAO

    abstract fun favouritePlaceDAO(): FavouritePlaceDao

    companion object {
        private const val NAME = "weather.db"

        @Volatile
        private var INSTANCE: WeatherDB? = null

        fun getInstance(context: Context): WeatherDB =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        WeatherDB::class.java, NAME)
                        .fallbackToDestructiveMigration()
                        .build()

        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE place "
                        + " ADD COLUMN isCurrent INTEGER")
            }
        }
        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }
    }


}

object ExecutorsUtil {
    private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

    /**
     * Utility method to run blocks on a dedicated background thread, used for io/database work.
     */
    fun ioThread(f: () -> Unit) {
        IO_EXECUTOR.execute(f)
    }
}