package com.lab.esh1n.weather.di

import android.app.Application
import com.lab.esh1n.weather.WeatherApp
import com.lab.esh1n.weather.di.base.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Created by esh1n on 3/7/18.
 */
@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    NetworkModule::class,
    DatabaseModule::class,
    ActivitiesModule::class,
    ServicesModule::class,
    ViewModelModule::class])
interface AppComponent {

    fun inject(app: WeatherApp)

    fun plusWorkerComponent(): WorkerComponent.Builder

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}
