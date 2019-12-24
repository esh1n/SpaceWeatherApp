package com.lab.esh1n.weather.weather.model

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import com.lab.esh1n.weather.R

class WeatherBackgroundModel(val code: String, val isDay: Boolean, val hourOfDay: Int, val clouds: Int, val rain: Int, val snow: Int)

class WeatherBackgroundUtil {
    //TODO выдели основные погодные типы - облачно - днем и ночью, дождь - днем и ночью, снег - днем и ночью, чистые небеса - днем и ночью и сделай для них фоны
    companion object {
        private fun getGradientBackgroundColors(weatherModel: WeatherBackgroundModel): Pair<Int, Int> {
            val color = getGradientBackgroundColor(weatherModel)
            val maxRainValue = weatherModel.clouds.coerceAtLeast(weatherModel.rain).coerceAtLeast(weatherModel.snow)
            return Pair(color, maxRainValue)
        }

        private fun getGradientBackgroundColor(weatherModel: WeatherBackgroundModel): Int {
            if (weatherModel.isDay) {
                return if (weatherModel.clouds == 0 && weatherModel.rain == 0 && weatherModel.snow == 0) {
                    R.array.clear_sky
                } else {
                    if (weatherModel.rain == 0 && weatherModel.clouds > 0) {
                        R.array.cloudy_sky
                    } else {
                        R.array.rain_sky
                    }

                }

            } else {
                return if (weatherModel.clouds == 0 && weatherModel.rain == 0 || weatherModel.snow == 0) {
                    R.array.night_clear_sky
                } else {
                    R.array.night_cloudy_sky
                }

            }
        }

        fun prepareWeatherGradient(context: Context, weatherBackgroundModel: WeatherBackgroundModel): GradientDrawable {
            val colorsPackAndRainValue = getGradientBackgroundColors(weatherBackgroundModel)
            val rainValue = colorsPackAndRainValue.second
            val colors = context.resources.getStringArray(colorsPackAndRainValue.first)
            val colorStart: Int = addColorSaturation(Color.parseColor(colors[0]), rainValue)
            val colorMiddle: Int = addColorSaturation(Color.parseColor(colors[1]), rainValue)
            val colorEnd: Int = addColorSaturation(Color.parseColor(colors[2]), rainValue)
            val gd = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(colorStart, colorMiddle, colorEnd))
            gd.cornerRadius = 0f
            return gd
        }

        fun addColorSaturation(color: Int, maxValue: Int): Int {
            val red = Color.red(color)
            val green = Color.green(color)
            val blue = Color.blue(color)
            val hsvColor = FloatArray(3)
            Color.RGBToHSV(red, green, blue, hsvColor)
            val newSaturation = ((maxValue / 100) * 0.99).toFloat()
            hsvColor[1] = newSaturation
            return Color.HSVToColor(hsvColor)
        }

        fun getColorForTime(isDay: Boolean): Int {
            return if (isDay) Color.BLACK else Color.WHITE
        }
    }
}