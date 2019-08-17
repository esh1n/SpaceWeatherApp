package com.lab.esh1n.weather.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.esh1n.utils_android.ui.getImage
import com.lab.esh1n.weather.R

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
fun convertPathToIcon(tv: ImageView, iconId: String?) {
    iconId?.let {
        val preparedIconId = tv.context.getImage(iconId)
        tv.setImageResource(preparedIconId)
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
