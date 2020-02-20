package com.lab.esh1n.weather

import android.app.Application
import android.content.Context
import com.lab.esh1n.data.cache.AppPrefs
import com.lab.esh1n.weather.di.component.AppComponent
import com.lab.esh1n.weather.di.component.DaggerAppComponent
import com.lab.esh1n.weather.di.component.WorkerComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import java.util.*
import javax.inject.Inject


/**
 * Created by esh1n on 3/9/18.
 */

class WeatherApp : Application(), HasAndroidInjector {


    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Any>


    @Inject
    lateinit var appPrefs: AppPrefs


    override fun androidInjector() = activityDispatchingAndroidInjector


    private lateinit var appComponent: AppComponent
    private lateinit var workerComponent: WorkerComponent


    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
                .builder()
                .application(this)
                .build()
        workerComponent = appComponent.plusWorkerComponent().build()
        appComponent.inject(this)

    }


    fun getLocaleBlocking(): Locale {
        return appPrefs.getLocale()
    }

    companion object {
        fun getWorkerComponent(context: Context): WorkerComponent {
            return (context as WeatherApp).workerComponent
        }
    }


}
