package com.lab.esh1n.weather.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.lab.esh1n.weather.R


private const val EVENT_KEYWORD = "Event"

@BindingAdapter("temperatureCelsius")
fun setTemperatureCelsius(tv: TextView, temperatureCelsius: Double?) {
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

@BindingAdapter(value = ["tempMin", "tempMax"], requireAll = false)
fun setTemperatureRange(tv: TextView, tempMin: Double?, tempMax: Double?) {
    if (tempMin != null && tempMax != null) {
        val tempRange = tv.context.getString(R.string.text_temperature_range_celsius, tempMin, tempMax)
        tv.text = tempRange
    }
}

fun getTypeDescription(type: String): String {
    return if (type.contains(EVENT_KEYWORD)) {
        type.replace(EVENT_KEYWORD, "")
    } else {
        EVENT_KEYWORD
    }
}