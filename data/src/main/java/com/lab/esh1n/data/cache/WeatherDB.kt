package com.lab.esh1n.data.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lab.esh1n.data.cache.dao.PlaceDAO
import com.lab.esh1n.data.cache.dao.WeatherDAO
import com.lab.esh1n.data.cache.entity.PlaceEntry
import com.lab.esh1n.data.cache.entity.WeatherEntry
import java.util.concurrent.Executors


/**
 * Created by esh1n on 3/7/18.
 */
@Database(entities = [WeatherEntry::class, PlaceEntry::class], version = 2, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class WeatherDB : RoomDatabase() {

    abstract fun weatherDAO(): WeatherDAO

    abstract fun placeDAO(): PlaceDAO

    companion object {
        const val NAME = "weather.db"
        @Volatile
        private var INSTANCE: WeatherDB? = null

        fun getInstance(context: Context): WeatherDB =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        WeatherDB::class.java, NAME)
                        // prepopulate the database after onCreate was called
                        .addMigrations(MIGRATION_1_2)
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                // insert the data on the IO Thread
                                ExecutorsUtil.ioThread {
                                    getInstance(context).placeDAO().insertPlaces(PREPOPULATE_DATA)
                                }
                            }
                        })
                        .build()

        val PREPOPULATE_DATA = listOf(PlaceEntry(472045, "Voronezh", "Europe/Moscow", true), PlaceEntry(524901, "MOSCOW", "Europe/Moscow", false))

        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE place "
                        + " ADD COLUMN isCurrent INTEGER");
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