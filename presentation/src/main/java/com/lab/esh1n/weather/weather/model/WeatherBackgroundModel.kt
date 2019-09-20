package com.lab.esh1n.weather.weather.model

class WeatherBackgroundModel(val day: Boolean, val clouds: Int, val rain: Int, val snow: Int) {

}

class WeatherBackgroundUtil {
    companion object {
        fun getGradientBackgroundColors(weatherModel: WeatherBackgroundModel): IntArray {
            if (weatherModel.day) {
                return if (weatherModel.clouds == 0 || weatherModel.rain == 0 || weatherModel.snow == 0) {
                    intArrayOf(0x00c0f5, 0x00d4fa, 0xd7f5f2)
                } else {
                    if (weatherModel.rain == 0) {
                        intArrayOf(0xa3a93c9, 0x60abd6, 0xe1e9ed)
                    } else {
                        intArrayOf(0xa3b6c9, 0xdfe9f2, 0x89b5d9)
                    }

                }

            } else {
                if (weatherModel.clouds == 0 || weatherModel.rain == 0 || weatherModel.snow == 0) {
                    return intArrayOf(0x03080d, 0x002f5e, 0x2f3494)
                } else {
                    return intArrayOf(0x07305e, 0x0780b0, 0x0bbbde)
                }

            }
        }
    }
}