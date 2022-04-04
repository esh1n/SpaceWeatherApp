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
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.data.cache.entity.Temperature
import com.lab.esh1n.weather.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.domain.IUILocalisator
import com.lab.esh1n.weather.presentation.MainActivity
import com.lab.esh1n.weather.presentation.mapper.DateFormat
import com.lab.esh1n.weather.presentation.mapper.UiDateListMapper

object NotificationUtil {

    private fun buildNotification(
        context: Context,
        weatherNotification: WeatherNotification = WeatherNotification.emptyNotification(context)
    ): Notification {
        val pendingIntent: PendingIntent =
            Intent(context, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(
                    context,
                    0,
                    notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )
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
    private fun createNotificationChannel(
        context: Context,
        channelId: String,
        channelName: String
    ): String {
        val chan =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    private fun mapResultToWeatherNotification(
        weather: WeatherWithPlace,
        ctx: Context,
        dateMapper: UiDateListMapper,
        temperatureMapper: (Temperature) -> OneValueProperty
    ): WeatherNotification {
        val title =
            ctx.getString(R.string.text_weather_notification_title, weather.description)
        val middleTemperature =
            weather.temperatureMin.middleTemperature(weather.temperatureMax)
        val localizedTemperature: String =
            temperatureMapper(middleTemperature).convertProperty(ctx)
        val weatherDate = dateMapper.map(weather.epochDateMills)
        val message = ctx.getString(
            R.string.text_weather_description,
            localizedTemperature,
            weather.placeName,
            weatherDate
        )
        return WeatherNotification(title, message, weather.iconId, title)

    }


    fun sendCurrentWeatherNotification(
        weatherWithPlace: WeatherWithPlace,
        localizedContext: Context,
        uiLocalizer: IUILocalisator
    ) {
        val dateMapper = uiLocalizer.provideDateMapper(
            weatherWithPlace.timezone
                ?: "UTC", DateFormat.HOUR
        )
        val temperatureMapper: (Temperature) -> OneValueProperty = { temperatureInCelsius ->
            uiLocalizer.localizeTemperature(temperatureInCelsius)
        }
        val weatherNotification = mapResultToWeatherNotification(
            weatherWithPlace,
            localizedContext,
            dateMapper,
            temperatureMapper
        )
        sendNotification(localizedContext, weatherNotification)


    }

    fun sendFailureWeatherNotification(
        localizedContext: Context,
        errorMessage: String?
    ) {
        val title = localizedContext.getString(R.string.error_loading_weather)
        val message = localizedContext.getString(R.string.hint_refresh, errorMessage)
        val weatherNotification = WeatherNotification(title, message, ticker = title)
        sendNotification(localizedContext, weatherNotification)

//    val title = ctx.getString(R.string.error_loading_weather)
//    val message = ctx.getString(R.string.hint_refresh, result.errorModel?.message)
//    return WeatherNotification(title, message, ticker = title)
    }


    fun sendNotification(localizedContext: Context, weatherNotification: WeatherNotification) {
        with(NotificationManagerCompat.from(localizedContext)) {
            // notificationId is a unique int for each notification that you must define
            notify(
                CURRENT_WEATHER_NOTIFICATION_ID,
                buildNotification(localizedContext, weatherNotification)
            )
        }
    }
}

private const val CURRENT_WEATHER_NOTIFICATION_ID = 1233219


data class WeatherNotification(
    val title: String,
    val text: String,
    val iconId: String = "01d",
    val ticker: String
) {
    companion object {
        fun emptyNotification(context: Context) = WeatherNotification(
            context.getString(R.string.notification_title),
            context.getString(R.string.notification_message),
            ticker = ""
        )
    }
}
