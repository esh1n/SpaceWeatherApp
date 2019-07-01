package com.lab.esh1n.weather.di.base

import com.lab.esh1n.weather.di.weather.WeatherUseCaseModule
import com.lab.esh1n.weather.weather.worker.SyncWeatherService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ServicesModule {
    @ContributesAndroidInjector(modules = [WeatherUseCaseModule::class])
    fun contributeWeatherServiceModule(): SyncWeatherService
}