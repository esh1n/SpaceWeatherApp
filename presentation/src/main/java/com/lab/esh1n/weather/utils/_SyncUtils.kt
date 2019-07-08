package com.lab.esh1n.weather.utils

import androidx.work.*
import com.lab.esh1n.weather.weather.worker.SyncAllDataWorker
import java.util.concurrent.TimeUnit

const val WORKER_ERROR_DESCRIPTION = "WORKER_ERROR_DESCRIPTION"

fun WorkManager.startPeriodicSync() {
    val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

    val syncAllDataWorker = PeriodicWorkRequest.Builder(SyncAllDataWorker::class.java, 5, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

    enqueueUniquePeriodicWork(SyncAllDataWorker::class.java.name, ExistingPeriodicWorkPolicy.REPLACE, syncAllDataWorker);


}