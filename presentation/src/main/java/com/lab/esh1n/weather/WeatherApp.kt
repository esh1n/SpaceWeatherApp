package com.lab.esh1n.weather

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Context
import androidx.work.WorkManager
import com.lab.esh1n.weather.di.component.AppComponent
import com.lab.esh1n.weather.di.component.DaggerAppComponent
import com.lab.esh1n.weather.di.component.WorkerComponent
import com.lab.esh1n.weather.utils.startCurrentPlacePeriodicSync
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import javax.inject.Inject


/**
 * Created by esh1n on 3/9/18.
 */

class WeatherApp : Application(), HasActivityInjector,HasServiceInjector {


    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var serviceDispatchingAndroidInjector: DispatchingAndroidInjector<Service>

    @Inject
    lateinit var workManager: WorkManager

    override fun serviceInjector() = serviceDispatchingAndroidInjector

    override fun activityInjector() =  activityDispatchingAndroidInjector

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
        workManager.startCurrentPlacePeriodicSync()
    }

    companion object {
        fun getWorkerComponent(context: Context): WorkerComponent {
            return (context as WeatherApp).workerComponent
        }
    }
}
