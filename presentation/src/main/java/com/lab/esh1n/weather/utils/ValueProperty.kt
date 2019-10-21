package com.lab.esh1n.weather.utils

import android.content.Context

sealed class ValueProperty(val stringRes: Int?)
class StringResValueProperty(stringRes: Int) : ValueProperty(stringRes)
class OneValueProperty(stringRes: Int, val value: String?) : ValueProperty(stringRes)

fun ValueProperty?.convertProperty(context: Context): String {
    if (this != null) {
        if (this is StringResValueProperty) {
            return context.getString(this.stringRes!!)
        }
        if (this is OneValueProperty) {
            return context.getString(this.stringRes!!, this.value)
        }
    }
    return ""
}
