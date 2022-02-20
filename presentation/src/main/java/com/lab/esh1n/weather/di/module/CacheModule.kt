package com.lab.esh1n.weather.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.AssetManager
import com.lab.esh1n.weather.data.cache.AppPrefs
import com.lab.esh1n.weather.data.cache.WeatherDB
import com.lab.esh1n.weather.domain.IUILocalisator
import com.lab.esh1n.weather.domain.prefs.IPrefsInteractor
import com.lab.esh1n.weather.weather.mapper.UILocalizerImpl
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
    fun provideAppPrefs(sharedPrefs: SharedPreferences): IPrefsInteractor {
        return AppPrefs(sharedPrefs)
    }

    @Provides
    @Singleton
    fun provideAssets(context: Context): AssetManager {
        return context.assets
    }

    @Provides
    @Singleton
    fun provideUiLocalizer(prefs: IPrefsInteractor): IUILocalisator {
        return UILocalizerImpl(prefs)
    }
}