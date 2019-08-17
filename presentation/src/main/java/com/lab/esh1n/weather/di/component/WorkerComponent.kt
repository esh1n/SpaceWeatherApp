package com.lab.esh1n.weather.di.component


import com.lab.esh1n.weather.di.worker.WorkerScope
import com.lab.esh1n.weather.weather.worker.PrePopulatePlacesWorker
import com.lab.esh1n.weather.weather.worker.SyncAllPlacesForecastWorker
import com.lab.esh1n.weather.weather.worker.SyncCurrentsWeatherWorker
//import com.lab.esh1n.weather.weather.worker.SyncCurrentWeatherWorker
import dagger.Subcomponent

@WorkerScope
@Subcomponent()
interface WorkerComponent {

    fun inject(worker: SyncCurrentsWeatherWorker)
    fun inject(worker: SyncAllPlacesForecastWorker)
    fun inject(worker: PrePopulatePlacesWorker)

    @Subcomponent.Builder
    interface Builder {
        fun build(): WorkerComponent
    }
}
