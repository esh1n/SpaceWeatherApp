package com.lab.esh1n.weather.weather.viewmodel

import android.app.Application
import androidx.work.WorkManager
import com.esh1n.core_android.rx.SchedulersFacade
import com.esh1n.core_android.ui.viewmodel.BaseAndroidViewModel
import com.esh1n.core_android.ui.viewmodel.Resource
import com.esh1n.core_android.ui.viewmodel.SingleLiveEvent
import com.google.android.gms.ads.MobileAds
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.WeatherApp
import com.lab.esh1n.weather.domain.ProgressModel
import com.lab.esh1n.weather.domain.places.usecase.PrePopulatePlacesUseCase
import com.lab.esh1n.weather.utils.startAllPlacesForecastPeriodicSync
import com.lab.esh1n.weather.utils.startCurrentPlacePeriodicSync
import javax.inject.Inject

class SplashVM @Inject
constructor(application: Application, private val workManager: WorkManager, private val prepopulateUseCase: PrePopulatePlacesUseCase) : BaseAndroidViewModel(application) {

    val prepopulateEvent = SingleLiveEvent<Resource<ProgressModel<Unit>>>()


    fun startPrepopulate() {
        initAdmob()
        prepopulateUseCase
                .perform(Unit)
                .compose(SchedulersFacade.applySchedulersFlowable())
                .subscribe({ models ->
                    if (models.status == Resource.Status.SUCCESS && models.data?.isDone == true) {
                        with(workManager) {
                            startCurrentPlacePeriodicSync()
                            startAllPlacesForecastPeriodicSync()
                        }
                    }
                    prepopulateEvent.postValue(models)
                }, { error -> prepopulateEvent.postValue(Resource.error(error)) })
                .disposeOnDestroy()
    }

    private fun initAdmob() {
        val context = getApplication<WeatherApp>()
        val appId = context.getString(R.string.admob_app_id)
        MobileAds.initialize(context, appId)
    }

}