package com.lab.esh1n.weather.utils

import androidx.work.*
import com.lab.esh1n.weather.weather.worker.SyncAllDataWorker
import java.util.concurrent.TimeUnit

const val WORKER_ERROR_DESCRIPTION = "WORKER_ERROR_DESCRIPTION"
const val SYNC_ID = "SYNC_ALL_DATA_WITH_SERVER"

fun WorkManager.startPeriodicSync() {
    val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

    val syncAllDataWorker = PeriodicWorkRequest.Builder(SyncAllDataWorker::class.java, 5, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

    enqueueUniquePeriodicWork(SYNC_ID, ExistingPeriodicWorkPolicy.KEEP, syncAllDataWorker);

}