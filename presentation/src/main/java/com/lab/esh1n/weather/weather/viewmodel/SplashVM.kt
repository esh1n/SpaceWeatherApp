package com.lab.esh1n.weather.weather.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.esh1n.core_android.ui.viewmodel.BaseAndroidViewModel
import com.google.android.gms.ads.MobileAds
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.WeatherApp
import com.lab.esh1n.weather.utils.observePrepopulateSync
import com.lab.esh1n.weather.utils.prepopulateDbAndStartSync
import javax.inject.Inject

class SplashVM @Inject
constructor(application: Application, private val workManager: WorkManager) : BaseAndroidViewModel(application) {
    fun startSync() {
        initAdmob()
        with(workManager) {
            prepopulateDbAndStartSync()
        }
    }

    private fun initAdmob() {
        val context = getApplication<WeatherApp>()
        val appId = context.getString(R.string.admob_app_id)
        MobileAds.initialize(context, appId);
    }

    fun observeSync(): LiveData<MutableList<WorkInfo>> {
        return workManager.observePrepopulateSync()
    }

}