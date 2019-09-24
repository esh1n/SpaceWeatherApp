package com.lab.esh1n.weather.weather.model

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import com.lab.esh1n.weather.R

class WeatherBackgroundModel(val code: String, val isDay: Boolean, val hourOfDay: Int, val clouds: Int, val rain: Int, val snow: Int)

class WeatherBackgroundUtil {
    companion object {
        private fun getGradientBackgroundColors(weatherModel: WeatherBackgroundModel): Int {
            if (!weatherModel.isDay) {
                return if (weatherModel.clouds > 15 || weatherModel.rain > 0 || weatherModel.snow > 0) {
                    R.array.night_cloudy_sky
                } else {
                    R.array.night_clear_sky
                }

            } else {

                return if (weatherModel.clouds == 0 || weatherModel.rain == 0 || weatherModel.snow == 0) {
                    R.array.clear_sky
                } else {
                    if (weatherModel.rain == 0 && weatherModel.clouds > 0) {
                        R.array.cloudy_sky
                    } else {
                        R.array.rain_sky
                    }

                }

            }
        }

        fun prepareWeatherGradient(context: Context, weatherBackgroundModel: WeatherBackgroundModel): GradientDrawable {
            val colors = context.resources.getStringArray(getGradientBackgroundColors(weatherBackgroundModel))
            val parsedColors = intArrayOf(Color.parseColor(colors[0]), Color.parseColor(colors[1]), Color.parseColor(colors[2]))
            val gd = GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM, parsedColors)
            gd.cornerRadius = 0f
            return gd

        }

        private fun isEvening(hourOfDay: Int): Boolean {
            return hourOfDay >= 19
        }


        fun getColorForTime(isDay: Boolean): Int {
            return if (isDay) Color.BLACK else Color.WHITE
        }
    }
}