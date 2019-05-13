package com.lab.esh1n.weather.di.base

import com.lab.esh1n.weather.weather.WeatherActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by esh1n on 3/9/18.
 */
@Module
interface ActivitiesModule {
    @ContributesAndroidInjector(modules = [FragmentsModule::class])
    fun contributeHomeActivity(): WeatherActivity
}