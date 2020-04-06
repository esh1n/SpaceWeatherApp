package com.lab.esh1n.data.cache

import android.content.SharedPreferences
import io.reactivex.Single
import java.util.*

class AppPrefs(sharedPreferences: SharedPreferences) : RxPrefs(sharedPreferences), IPrefsProvider {

    override fun getLocale(): Locale {
        return Locale.forLanguageTag(getString(KEY_LOCALE, DEFAULT_LOCALE))
    }

    override fun getAppTemperatureUnits(): TemperatureUnit {
        return TemperatureUnit.valueOf(getString(KEY_TEMPERATURE_UNITS, DEFAULT_TEMPERATURE_UNITS))
    }

    override fun getServerAPITemperatureUnits() = TemperatureUnit.C

    override fun getAppUnits(): Units {
        return Units.valueOf(getString(KEY_UNITS, DEFAULT_UNITS))
    }

    override fun getServerAPIUnits() = Units.METRIC

    fun getLanguageAndServerUnits(): Pair<String, String> {
        return Pair(getLocale().language, getServerAPIUnits().serverValue)
    }

    fun getLanguageAndServerUnitsSingle(): Single<Pair<String, String>> {
        return Single.fromCallable {
            return@fromCallable getLanguageAndServerUnits()
        }
    }

    fun getLanguageSingle(): Single<String> {
        return Single.fromCallable {
            getLocale().language
        }
    }

    companion object {
        const val KEY_LOCALE = "Locale"
        const val KEY_UNITS = "Units"
        const val KEY_TEMPERATURE_UNITS = "TemperatureUnits"
        const val DEFAULT_LOCALE = "ru-RU"
        val DEFAULT_UNITS = Units.METRIC.name
        val DEFAULT_TEMPERATURE_UNITS = TemperatureUnit.C.name
    }
}