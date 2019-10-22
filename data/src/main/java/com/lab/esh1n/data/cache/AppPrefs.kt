package com.lab.esh1n.data.cache

import android.content.SharedPreferences
import io.reactivex.Single
import java.util.*

class AppPrefs(sharedPreferences: SharedPreferences) : RxPrefs(sharedPreferences) {
    companion object {
        const val KEY_LOCALE = "Locale"
        const val KEY_UNITS = "Units"
        const val DEFAULT_LOCALE = "ru-RU"
    }

    fun getCurrentLocale(): Locale {
        return Locale.forLanguageTag(getString(KEY_LOCALE, DEFAULT_LOCALE))
    }

    fun getUnits(): String {
        return getString(KEY_UNITS, Units.metric.name)
    }

    fun getLangAndUnits(): Pair<String, String> {
        return Pair(getCurrentLocale().language, getUnits())
    }

    fun getLangAndUnitsSingle(): Single<Pair<String, String>> {
        return Single.fromCallable {
            Pair(getCurrentLocale().language, getUnits())
        }
    }

    enum class Units {
        metric, imperial
    }
}