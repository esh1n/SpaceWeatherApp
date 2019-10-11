package com.lab.esh1n.weather.di.component

import android.app.Application
import com.lab.esh1n.weather.WeatherApp
import com.lab.esh1n.weather.di.module.*
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
    CacheModule::class,
    ActivitiesModule::class,
    RepositoriesModule::class,
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
