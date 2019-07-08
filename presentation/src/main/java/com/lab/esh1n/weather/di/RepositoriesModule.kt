package com.lab.esh1n.weather.di

import android.content.SharedPreferences
import com.lab.esh1n.data.api.APIService
import com.lab.esh1n.data.cache.WeatherDB
import com.lab.esh1n.weather.domain.weather.WeatherRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class RepositoriesModule {

    @Provides
    @Singleton
    fun provideWeatherRepository(userSessionApiService: APIService, database: WeatherDB, preferences: SharedPreferences): WeatherRepository {
        return WeatherRepository(userSessionApiService, database, preferences)
    }

}
