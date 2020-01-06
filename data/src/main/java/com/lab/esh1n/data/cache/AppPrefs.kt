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

    fun getUnits(): Units {
        return Units.valueOf(getString(KEY_UNITS, Units.metric.name))
    }

    fun getLangAndUnits(): Pair<String, Units> {
        return Pair(getCurrentLocale().language, getUnits())
    }

    fun getLangAndUnitsSingle(): Single<Pair<String, Units>> {
        return Single.fromCallable {
            Pair(getCurrentLocale().language, getUnits())
        }
    }

    enum class Units {
        //TODO make special method for server values
        metric, imperial
    }
}