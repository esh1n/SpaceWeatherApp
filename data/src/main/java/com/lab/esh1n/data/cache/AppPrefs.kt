package com.lab.esh1n.data.cache

import android.content.SharedPreferences
import io.reactivex.Single
import java.util.*

class AppPrefs(sharedPreferences: SharedPreferences) : RxPrefs(sharedPreferences), IPrefsProvider {


    //TODO save current timezone and use it in calls to DB
    override fun getLocale(): Locale {
        return Locale.forLanguageTag(getString(KEY_LOCALE, DEFAULT_LOCALE))
    }

    override fun getTemperatureUnits(): TemperatureUnit {
        return TemperatureUnit.valueOf(getString(KEY_TEMPERATURE_UNITS, DEFAULT_TEMPERATURE_UNITS))
    }

    override fun getUnits(): Units {
        return Units.valueOf(getString(KEY_UNITS, DEFAULT_UNITS))
    }

    fun getLangAndUnits(): Pair<String, String> {
        return Pair(getLocale().language, getUnits().serverValue)
    }

    fun getLangAndUnitsSingle(): Single<Pair<String, String>> {
        return Single.fromCallable {
            Pair(getLocale().language, getUnits().serverValue)
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