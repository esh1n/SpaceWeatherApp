package com.lab.esh1n.weather.weather.worker

import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.work.WorkManager
import com.lab.esh1n.data.cache.entity.WeatherEntity
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.base.BaseCoroutineService
import com.lab.esh1n.weather.domain.base.Resource
import com.lab.esh1n.weather.domain.weather.usecases.LoadWeatherByCityFromDBUseCase
import com.lab.esh1n.weather.utils.ForegroundServiceLauncher
import com.lab.esh1n.weather.utils.NotificationUtil
import com.lab.esh1n.weather.utils.NotificationUtil.Companion.buildNotification
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
        startForeground(ONGOING_NOTIFICATION_ID, buildNotification(this))
        LAUNCHER.onServiceCreated(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        loadWeather()
        return START_NOT_STICKY
    }

    private fun loadWeather() {
        if (loadJob?.isActive == true) return
        loadJob = launch {
            val result = withContext(Dispatchers.IO) { loadWeatherByCityFromDBUseCase.execute("Voronezh") }
            val notification = mapResultToWeatherNotification(result)
            startForeground(notification)
        }
    }

    private fun startForeground(notification: NotificationUtil.WeatherNotification) {
        startForeground(ONGOING_NOTIFICATION_ID, buildNotification(this, notification))
    }

    private fun mapResultToWeatherNotification(result: Resource<WeatherEntity>): NotificationUtil.WeatherNotification {
        when (result.status) {
            Resource.Status.SUCCESS -> {
                val weather = result.data!!
                val title = "В ${weather.cityName} ${weather.description}"
                val message = "Температура с ${weather.temperatureMin} до ${weather.temperatureMax}"
                val resourceName = "status_${weather.iconId}"
                return NotificationUtil.WeatherNotification(title, message, resourceName, title)
            }

            Resource.Status.ERROR -> {
                val title = getString(R.string.error_loading_weather)
                val message = getString(R.string.hint_refresh)
                return NotificationUtil.WeatherNotification(title, message, ticker = title)
            }

            else -> return NotificationUtil.WeatherNotification.emptyNotification(this)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }


}