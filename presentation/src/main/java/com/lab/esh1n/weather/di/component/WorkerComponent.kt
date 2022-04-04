package com.lab.esh1n.weather.di.component


import com.lab.esh1n.weather.di.worker.WorkerScope
import com.lab.esh1n.weather.weather.worker.SyncAllPlacesForecastWorker
import com.lab.esh1n.weather.weather.worker.SyncCurrentsWeatherWorker
import dagger.Subcomponent

@WorkerScope
@Subcomponent()
interface WorkerComponent {

    fun inject(worker: SyncCurrentsWeatherWorker)
    fun inject(worker: SyncAllPlacesForecastWorker)

    @Subcomponent.Builder
    interface Builder {
        fun build(): WorkerComponent
    }
}
