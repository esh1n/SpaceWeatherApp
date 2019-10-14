package com.lab.esh1n.weather.utils

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.weather.model.WeatherBackgroundUtil

@BindingAdapter("temperatureCelsius")
fun setTemperatureCelsius(tv: TextView, temperatureCelsius: Int?) {
    temperatureCelsius?.let {
        val temp = tv.context.getString(R.string.text_temperature_celsius, it)
        tv.text = temp
    }

}

@BindingAdapter("intAsString")
fun setIntAsString(tv: TextView, value: Int?) {
    value?.let {
        tv.text = value.toString()
    }

}

@BindingAdapter("floatAsString")
fun setFloatAsString(tv: TextView, value: Float?) {
    value?.let {
        tv.text = value.toString()
    }
}

@BindingAdapter("convertPathToIcon")
fun getWeatherIconRes(tv: ImageView, iconId: String?) {
    iconId?.let {
        val preparedIconId = tv.context.getImage(iconId, "ic_")
        tv.setImageResource(preparedIconId)
    }
}

@BindingAdapter("txtColor")
fun getTextColor(tv: TextView, isDay: Boolean?) {
    isDay?.let {
        tv.setTextColor(WeatherBackgroundUtil.getColorForTime(isDay))
    }
}

@BindingAdapter("lastUpdateTime")
fun getLastUpdateTime(tv: TextView, lastUpdateTime: String?) {
    lastUpdateTime?.let {
        tv.text = tv.context.getString(R.string.text_last_update_time, lastUpdateTime)
    }
}

@BindingAdapter(value = ["tempMin", "tempMax"], requireAll = false)
fun setTemperatureRange(tv: TextView, tempMin: Int?, tempMax: Int?) {
    if (tempMin != null && tempMax != null) {
        val tempRange = if (tempMin != tempMax) tv.context.getString(R.string.text_temperature_range_celsius, tempMin, tempMax) else
            tv.context.getString(R.string.text_temperature_no_range_celsius, tempMin)
        tv.text = tempRange
    }
}

fun Context.getImage(iconId: String, prefix: String = ""): Int {
    val res = "${prefix}status_$iconId"
    return resources.getIdentifier(res, "drawable", packageName)
}

