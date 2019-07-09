package com.lab.esh1n.weather.di.module

import com.lab.esh1n.weather.di.weather.WeatherUseCasesModule
import com.lab.esh1n.weather.weather.worker.SyncWeatherService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ServicesModule {
    @ContributesAndroidInjector(modules = [WeatherUseCasesModule::class])
    fun contributeWeatherServiceModule(): SyncWeatherService
}