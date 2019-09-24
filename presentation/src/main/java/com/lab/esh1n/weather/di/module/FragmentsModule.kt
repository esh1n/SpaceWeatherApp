package com.lab.esh1n.weather.di.module


import com.lab.esh1n.weather.di.weather.WeatherUseCasesModule
import com.lab.esh1n.weather.weather.fragment.AllPlacesFragment
import com.lab.esh1n.weather.weather.fragment.CurrentPlaceFragment
import com.lab.esh1n.weather.weather.fragment.SettingsFragment
import com.lab.esh1n.weather.weather.fragment.WeatherHostFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by esh1n on 3/9/18.
 */

@Module
interface FragmentsModule {

    @ContributesAndroidInjector(modules = [WeatherUseCasesModule::class])
    fun buildWeatherFragment(): CurrentPlaceFragment

    @ContributesAndroidInjector()
    fun buildSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector()
    fun buildWeatherHostFragment(): WeatherHostFragment

    @ContributesAndroidInjector()
    fun buildAllPlaces(): AllPlacesFragment

}