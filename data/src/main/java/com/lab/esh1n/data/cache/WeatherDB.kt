package com.lab.esh1n.data.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lab.esh1n.data.cache.dao.PlaceDAO
import com.lab.esh1n.data.cache.dao.WeatherDAO
import com.lab.esh1n.data.cache.entity.PlaceEntry
import com.lab.esh1n.data.cache.entity.WeatherEntry
import java.util.concurrent.Executors

/**
 * Created by esh1n on 3/7/18.
 */
@Database(entities = [WeatherEntry::class, PlaceEntry::class], version = 1, exportSchema = false)
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

        val PREPOPULATE_DATA = listOf(PlaceEntry(1, "Voronezh"), PlaceEntry(2, "MOSCOW"))
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