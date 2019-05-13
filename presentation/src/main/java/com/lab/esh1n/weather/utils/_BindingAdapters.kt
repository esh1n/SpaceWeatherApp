package com.lab.esh1n.weather.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.lab.esh1n.weather.R

private const val EVENT_KEYWORD = "Event"

@BindingAdapter("eventType")
fun setEventType(tv: TextView, type: String?) {
    val eventValue = if (type == null) tv.context.getString(R.string.text_event_type_update) else getTypeDescription(type)
    val eventWithPrefix = tv.context.getString(R.string.text_event_placeholder, eventValue)
    tv.text = eventWithPrefix
}

fun getTypeDescription(type: String): String {
    return if (type.contains(EVENT_KEYWORD)) {
        type.replace(EVENT_KEYWORD, "")
    } else {
        EVENT_KEYWORD
    }
}