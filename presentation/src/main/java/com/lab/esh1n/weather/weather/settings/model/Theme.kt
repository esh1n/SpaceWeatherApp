package com.lab.esh1n.weather.weather.settings.model

import androidx.annotation.StringRes
import com.lab.esh1n.weather.R

enum class Theme(@StringRes val label: Int) {
    LIGHT(R.string.theme_light),
    DARK(R.string.theme_dark),
    SYSTEM(R.string.theme_system)
}