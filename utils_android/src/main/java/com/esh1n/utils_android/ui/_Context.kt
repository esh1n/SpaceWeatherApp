package com.esh1n.utils_android.ui

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.core.content.ContextCompat
import java.util.*

fun Context.getColorCompat(colorResource: Int): Int {
    return ContextCompat.getColor(this, colorResource)
}

fun Context.getLocalizedResources(desiredLocale: Locale): Resources {
    return getLocalizedContext(desiredLocale).resources
}

fun Context.getLocalizedContext(desiredLocale: Locale): Context {
    var conf = resources.configuration
    conf = Configuration(conf)
    conf.setLocale(desiredLocale)
    return createConfigurationContext(conf)
}