package com.lab.esh1n.weather.weather.worker

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.WorkManager
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.base.BaseCoroutineService
import com.lab.esh1n.weather.base.BaseObserver
import com.lab.esh1n.weather.domain.base.ErrorModel
import com.lab.esh1n.weather.domain.weather.LoadWeatherByCityFromDBUseCase
import com.lab.esh1n.weather.utils.ForegroundServiceLauncher
import com.lab.esh1n.weather.utils.NotificationUtil.Companion.buildNotification
import com.lab.esh1n.weather.utils.SnackbarBuilder
import com.lab.esh1n.weather.weather.WeatherActivity
import com.lab.esh1n.weather.weather.WeatherModel
import com.lab.esh1n.weather.weather.viewmodel.WeatherViewModel
import dagger.android.AndroidInjection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class SyncWeatherService : BaseCoroutineService() {

    @Inject
    lateinit var loadWeatherByCityFromDBUseCase: LoadWeatherByCityFromDBUseCase

    @Inject
    lateinit var workManager: WorkManager

    private var loadJob: Job? = null


    private lateinit var weatherViewModel: WeatherViewModel


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        private val LAUNCHER = ForegroundServiceLauncher(SyncWeatherService::class.java)

        @JvmStatic
        fun start(context: Context) = LAUNCHER.startService(context)

        @JvmStatic
        fun stop(context: Context) = LAUNCHER.stopService(context)

        const val ONGOING_NOTIFICATION_ID = 123
    }

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
        weatherViewModel = WeatherViewModel(loadWeatherByCityFromDBUseCase,workManager)
        startForeground(ONGOING_NOTIFICATION_ID, buildNotification(this))
        LAUNCHER.onServiceCreated(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //...
        return START_NOT_STICKY
    }

    private fun loadWeather(){

    }



    override fun onDestroy() {
        super.onDestroy()
    }




}