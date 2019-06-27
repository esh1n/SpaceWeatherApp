package com.lab.esh1n.weather.weather.worker

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.utils.ForegroundServiceLauncher
import com.lab.esh1n.weather.weather.WeatherActivity


class SyncWeatherService : Service() {

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
        const val CHANNEL_DEFAULT_IMPORTANCE = "Weather"
    }

    override fun onCreate() {
        super.onCreate()

        val channelId =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createNotificationChannel("my_service", "My Background Service")
                } else {
                    // If earlier version channel ID is not used
                    // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                    ""
                }
        val pendingIntent: PendingIntent =
                Intent(this, WeatherActivity::class.java).let { notificationIntent ->
                    PendingIntent.getActivity(this, 0, notificationIntent, 0)
                }

        val notification: Notification = NotificationCompat.Builder(this, channelId)
                .setContentTitle(getText(R.string.notification_title))
                .setContentText(getText(R.string.notification_message))
                .setSmallIcon(R.drawable.ic_humidity)
                .setContentIntent(pendingIntent)
                .setTicker(getText(R.string.ticker_text))
                .build()

        startForeground(ONGOING_NOTIFICATION_ID, notification)

        LAUNCHER.onServiceCreated(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //...
        return Service.START_NOT_STICKY
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String{
        val chan = NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }


}