package com.lab.esh1n.weather.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.weather.WeatherActivity

class NotificationUtil {
    companion object {
        fun buildNotification(context: Context,
                              weatherNotification: WeatherNotification = WeatherNotification.emptyNotification(context)): Notification {
            val pendingIntent: PendingIntent =
                    Intent(context, WeatherActivity::class.java).let { notificationIntent ->
                        PendingIntent.getActivity(context, 0, notificationIntent, 0)
                    }


            val drawableResourceId = context.resources.getIdentifier(weatherNotification.resourceName, "drawable", context.packageName)
            return NotificationCompat.Builder(context, prepareChannelId(context))
                    .setContentTitle(weatherNotification.title)
                    .setContentText(weatherNotification.text)
                    .setSmallIcon(drawableResourceId)
                    .setLargeIcon(BitmapFactory.decodeResource(context.resources, drawableResourceId))
                    .setContentIntent(pendingIntent)
                    .setTicker(weatherNotification.ticker)
                    .build()
        }

        private fun prepareChannelId(context: Context): String {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(context, "my_service", "My Background Service")
            } else {
                // If earlier version channel ID is not used
                // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                ""
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun createNotificationChannel(context: Context, channelId: String, channelName: String): String {
            val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            chan.lightColor = Color.BLUE
            chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            val service = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(chan)
            return channelId
        }
    }

    data class WeatherNotification(val title: String, val text: String, val resourceName: String, val ticker: String) {
        companion object {
            fun emptyNotification(context: Context) = WeatherNotification(
                    context.getString(R.string.notification_title),
                    context.getString(R.string.notification_message),
                    "status_01d", ticker = ""
            )
        }
    }
}