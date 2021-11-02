package com.lab.esh1n.weather.di.module

import android.content.res.AssetManager
import com.lab.esh1n.weather.data.api.APIService
import com.lab.esh1n.weather.data.cache.AppPrefs
import com.lab.esh1n.weather.data.cache.WeatherDB
import com.lab.esh1n.weather.domain.places.PlacesRepository
import com.lab.esh1n.weather.domain.weather.WeatherRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class RepositoriesModule {

    @Provides
    @Singleton
    fun provideWeatherRepository(userSessionApiService: APIService, database: WeatherDB, appPrefs: AppPrefs): WeatherRepository {
        return WeatherRepository(userSessionApiService, database, appPrefs)
    }

    @Provides
    @Singleton
    fun providePlaceRepository(userSessionApiService: APIService, database: WeatherDB, appPrefs: AppPrefs, assetManager: AssetManager): PlacesRepository {
        return PlacesRepository(userSessionApiService, database, appPrefs, assetManager)
    }

}
