package com.lab.esh1n.weather.di.module

import android.app.Application
import android.content.SharedPreferences
import com.lab.esh1n.data.cache.WeatherDB
import com.lab.esh1n.data.cache.entity.AppSettingsInteractor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CacheModule {
    @Provides
    @Singleton
    fun provideDataBase(application: Application): WeatherDB {
        return WeatherDB.getInstance(application)
    }

    @Provides
    @Singleton
    fun provideSettingsInteractor(sharedPrefs: SharedPreferences): AppSettingsInteractor {
        return AppSettingsInteractor(sharedPrefs)
    }

}