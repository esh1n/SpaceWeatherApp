package com.lab.esh1n.weather.weather.model

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import com.lab.esh1n.weather.R

sealed class WeatherBackgroundModel(val code: String, val isDay: Boolean, val hourOfDay: Int, val clouds: Int, val rain: Int, val snow: Int)
class NoDataBackgroundModel(isDay: Boolean) : WeatherBackgroundModel("00d", isDay, 0, 0, 0, 0)
class SimpleBackgroundModel(code: String, isDay: Boolean, hourOfDay: Int, clouds: Int, rain: Int, snow: Int)
    : WeatherBackgroundModel(code, isDay, hourOfDay, clouds, rain, snow)

class WeatherBackgroundUtil {
    //TODO нагенери картинок на все weather code и вставь их, пока не сделаешь кастомное вью по 1)типу -коду, снег с дождем или переменная облачность 2) кол-ву снега, дождя, ОБЛАЧНОСТИ, времени суток
    companion object {
        private fun getGradientBackgroundColors(weatherModel: WeatherBackgroundModel): Pair<Int, Int> {
            val color = getGradientBackgroundColor(weatherModel)
            val maxRainValue = weatherModel.clouds.coerceAtLeast(weatherModel.rain).coerceAtLeast(weatherModel.snow)
            return Pair(color, maxRainValue)
        }

        private fun getGradientBackgroundColor(weatherModel: WeatherBackgroundModel): Int {
            if (weatherModel.isDay) {
                if (weatherModel is NoDataBackgroundModel) {
                    return R.array.no_data_sky_day
                }
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
                if (weatherModel is NoDataBackgroundModel) {
                    return R.array.no_data_sky_night
                }
                return if (weatherModel.clouds == 0 && weatherModel.rain == 0 || weatherModel.snow == 0) {
                    R.array.night_clear_sky
                } else {
                    R.array.night_cloudy_sky
                }

            }
        }

        fun prepareWeatherGradient(context: Context
                                   , weatherBackgroundModel: WeatherBackgroundModel): GradientDrawable {
            val colorsPackAndRainValue = getGradientBackgroundColors(weatherBackgroundModel)
            val colors = context.resources.getStringArray(colorsPackAndRainValue.first)
            // val colorStart: Int = addColorSaturation(Color.parseColor(colors[0]), rainValue)
            val colorStart: Int = Color.parseColor(colors[0])
            val colorMiddle: Int = Color.parseColor(colors[1])
            val colorEnd: Int = Color.parseColor(colors[2])
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

    enum class WeatherCode(val code: Int) {
        CLEAR(1), FEW_CLOUDS(2), SCATTERED_CLOUDS(3),
        BROKEN_CLOUDS(4), SHOWER_RAIN(9), RAIN(10), THUNDERSTORM(11),
        SNOW(13), MIST(50)
    }
}