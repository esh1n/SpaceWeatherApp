package com.lab.esh1n.weather.weather.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.esh1n.core_android.ui.viewmodel.BaseViewModel
import com.lab.esh1n.weather.utils.observePrepopulateSync
import com.lab.esh1n.weather.utils.prepopulateDbAndStartSync
import javax.inject.Inject

class SplashViewModel @Inject
constructor(application: Application, private val workManager: WorkManager) : BaseViewModel(application) {
    fun startSync() {
        with(workManager) {
            prepopulateDbAndStartSync()
        }
    }

    fun observeSync(): LiveData<MutableList<WorkInfo>> {
        return workManager.observePrepopulateSync()
    }

}