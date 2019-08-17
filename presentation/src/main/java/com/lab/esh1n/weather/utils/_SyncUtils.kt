package com.lab.esh1n.weather.utils

import androidx.work.*
import com.lab.esh1n.weather.weather.worker.PrePopulatePlacesWorker
import com.lab.esh1n.weather.weather.worker.SyncAllPlacesForecastWorker
import com.lab.esh1n.weather.weather.worker.SyncCurrentsWeatherWorker
import java.util.concurrent.TimeUnit

const val WORKER_ERROR_DESCRIPTION = "WORKER_ERROR_DESCRIPTION"

fun WorkManager.startCurrentPlacePeriodicSync() {
    val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

    val syncAllDataWorker = PeriodicWorkRequest.Builder(SyncCurrentsWeatherWorker::class.java, 1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()
    enqueueUniquePeriodicWork(SyncCurrentsWeatherWorker::class.java.name, ExistingPeriodicWorkPolicy.REPLACE, syncAllDataWorker)
}

fun WorkManager.startAllPlacesForecastPeriodicSync() {
    val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    val worker = PeriodicWorkRequest.Builder(SyncAllPlacesForecastWorker::class.java, 1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()
    enqueueUniquePeriodicWork(SyncAllPlacesForecastWorker::class.java.name, ExistingPeriodicWorkPolicy.REPLACE, worker)
}

fun WorkManager.prepopulateDbAndStartSync() {
    val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    val prepopulate = OneTimeWorkRequest.Builder(PrePopulatePlacesWorker::class.java)
            .setConstraints(constraints)
            .build()
    enqueue(prepopulate)
}
