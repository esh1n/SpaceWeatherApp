package com.lab.esh1n.weather.di.module


import com.lab.esh1n.weather.di.weather.WeatherUseCasesModule
import com.lab.esh1n.weather.weather.favourite.FavouritePlacesFragment
import com.lab.esh1n.weather.weather.fragment.*
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
    fun buildFavouritePlacesFragment(): FavouritePlacesFragment

    @ContributesAndroidInjector()
    fun buildSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector()
    fun buildWeatherHostFragment(): WeatherHostFragment

    @ContributesAndroidInjector()
    fun buildAllPlaces(): SearchPlacesFragment

    @ContributesAndroidInjector()
    fun buildForecastFragment(): ForecastFragment

    @ContributesAndroidInjector()
    fun buildSplashFragment(): SplashFragment

    @ContributesAndroidInjector()
    fun buildDayForecastFragment(): DayForecastFragment

}