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
import androidx.core.app.NotificationManagerCompat
import com.esh1n.core_android.ui.viewmodel.Resource
import com.esh1n.utils_android.ui.getLocalizedContext
import com.lab.esh1n.data.cache.entity.Temperature
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.weather.WeatherActivity
import com.lab.esh1n.weather.weather.mapper.DateFormat
import com.lab.esh1n.weather.weather.mapper.UiDateListMapper
import com.lab.esh1n.weather.weather.mapper.UiLocalizer

class NotificationUtil {
    companion object {

        private const val TAG = "Notification"

        private fun buildNotification(context: Context,
                                      weatherNotification: WeatherNotification = WeatherNotification.emptyNotification(context)): Notification {
            val pendingIntent: PendingIntent =
                    Intent(context, WeatherActivity::class.java).let { notificationIntent ->
                        PendingIntent.getActivity(context, 0, notificationIntent, 0)
                    }


            val drawableResourceId = context.getWeatherStatusImage(weatherNotification.iconId)
            return NotificationCompat.Builder(context, prepareChannelId(context))
                    .setContentTitle(weatherNotification.title)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(weatherNotification.text))
                    .setContentText(weatherNotification.text)
                    .setSmallIcon(drawableResourceId)
                    .setOngoing(true)
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

        private fun mapResultToWeatherNotification(result: Resource<WeatherWithPlace>, ctx: Context, dateMapper: UiDateListMapper, temperatureMapper: (Temperature) -> OneValueProperty): WeatherNotification {

            when (result.status) {
                Resource.Status.SUCCESS -> {
                    val weather = result.data!!
                    val title = ctx.getString(R.string.text_weather_notification_title, weather.description)
                    val middleTemperature = weather.temperatureMin.middleTemperature(weather.temperatureMax)
                    val localizedTemperature: String = temperatureMapper(middleTemperature).convertProperty(ctx)
                    val weatherDate = dateMapper.map(weather.epochDateMills)
                    val message = ctx.getString(R.string.text_weather_description, localizedTemperature, weather.placeName, weatherDate)
                    return WeatherNotification(title, message, weather.iconId, title)
                }

                Resource.Status.ERROR -> {
                    val title = ctx.getString(R.string.error_loading_weather)
                    val message = ctx.getString(R.string.hint_refresh, result.errorModel?.message)
                    return WeatherNotification(title, message, ticker = title)
                }

                else -> return WeatherNotification.emptyNotification(ctx)
            }
        }

        fun sendCurrentWeatherNotification(result: Resource<WeatherWithPlace>, context: Context, uiLocalizer: UiLocalizer) {
            val localizedContext = context.getLocalizedContext(uiLocalizer.getLocale())
            val dateMapper = uiLocalizer.provideDateMapper(result.data?.timezone
                    ?: "UTC", DateFormat.HOUR)
            val temperatureMapper: (Temperature) -> OneValueProperty = { temperatureInCelsius ->
                uiLocalizer.localizeTemperature(temperatureInCelsius)
            }
            val weatherNotification = mapResultToWeatherNotification(result, localizedContext, dateMapper, temperatureMapper)
            with(NotificationManagerCompat.from(localizedContext)) {
                // notificationId is a unique int for each notification that you must define
                notify(CURRENT_WEATHER_NOTIFICATION_ID, buildNotification(localizedContext, weatherNotification))
            }
        }

        private const val CURRENT_WEATHER_NOTIFICATION_ID = 1233219
    }

    data class WeatherNotification(val title: String, val text: String, val iconId: String = "01d", val ticker: String) {
        companion object {
            fun emptyNotification(context: Context) = WeatherNotification(
                    context.getString(R.string.notification_title),
                    context.getString(R.string.notification_message),
                    ticker = ""
            )
        }
    }
}