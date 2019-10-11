package com.lab.esh1n.data.cache.entity

import android.content.SharedPreferences
import com.lab.esh1n.data.cache.SettingsInteractor
import java.util.*

class AppSettingsInteractor(sharedPreferences: SharedPreferences) : SettingsInteractor(sharedPreferences) {
    companion object {
        const val KEY_LOCALE = "Locale"
        const val DEFAULT_LOCALE = "ru-RU"
    }

    fun getCurrentLocale(): Locale {
        return Locale.forLanguageTag(getString(KEY_LOCALE, DEFAULT_LOCALE))
    }
}