package com.esh1n.utils_android.ui

import android.content.Context
import android.content.res.Configuration
import java.util.*

object ContextUtil {
    fun getLocalizedContext(context: Context, desiredLocale: Locale): Context {
        return with(context) {
            createConfigurationContext(Configuration(resources.configuration).apply {
                setLocale(desiredLocale)
            })
        }
    }
}