package com.lab.esh1n.weather.di.component


import com.lab.esh1n.weather.di.worker.WorkerScope
//import com.lab.esh1n.weather.weather.worker.SyncAllDataWorker
import dagger.Subcomponent

@WorkerScope
@Subcomponent()
interface WorkerComponent {

    // fun inject(worker: SyncAllDataWorker)

    @Subcomponent.Builder
    interface Builder {
        fun build(): WorkerComponent
    }
}
