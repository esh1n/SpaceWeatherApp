package com.lab.esh1n.weather.di.base


import com.lab.esh1n.weather.di.weather.EventsModule
import com.lab.esh1n.weather.weather.fragment.WeatherFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by esh1n on 3/9/18.
 */

@Module
interface FragmentsModule {

    @ContributesAndroidInjector(modules = [EventsModule::class])
    fun buildWeatherFragment(): WeatherFragment

}