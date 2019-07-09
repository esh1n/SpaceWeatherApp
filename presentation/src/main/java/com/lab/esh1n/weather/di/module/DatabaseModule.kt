package com.lab.esh1n.weather.di.module

import android.app.Application
import androidx.room.Room
import com.lab.esh1n.data.cache.WeatherDB
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDataBase(application: Application): WeatherDB {
        return Room.databaseBuilder(application, WeatherDB::class.java, WeatherDB.NAME)
                .fallbackToDestructiveMigration()
                .build()
    }

}