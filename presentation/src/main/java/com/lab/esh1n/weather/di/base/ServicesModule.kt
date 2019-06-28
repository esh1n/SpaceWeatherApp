package com.lab.esh1n.weather.di.base

import com.lab.esh1n.weather.di.weather.EventsModule
import com.lab.esh1n.weather.weather.WeatherActivity
import com.lab.esh1n.weather.weather.worker.SyncWeatherService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ServicesModule {
    @ContributesAndroidInjector(modules = [EventsModule::class])
    fun contributeWeatherServiceModule(): SyncWeatherService
}