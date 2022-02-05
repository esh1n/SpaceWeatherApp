package com.lab.esh1n.weather.data.cache

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.lab.esh1n.weather.domain.IPrefsInteractor
import com.lab.esh1n.weather.domain.TemperatureUnit
import com.lab.esh1n.weather.domain.Units
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import java.util.*

class AppPrefs(private val sharedPreferences: SharedPreferences) : IPrefsInteractor {
    //TODO move passing gson from DI
    private val gson: Gson = Gson()

    override fun getLocale(): Single<Locale> {
        return sharedPreferences.get {
            val localeStr = getString(KEY_LOCALE, DEFAULT_LOCALE)!!
            Locale.forLanguageTag(localeStr)
        }
    }

    override fun getLanguage(): Single<String> = getLocale().map { it.language }

    override fun getLocaleBlocking(): Locale = getLocale().blockingGet()

    override fun getAppTemperatureUnits(): TemperatureUnit {
        return sharedPreferences.getString(KEY_TEMPERATURE_UNITS, DEFAULT_TEMPERATURE_UNITS)!!
            .let(TemperatureUnit::valueOf)
    }

    override fun getServerAPITemperatureUnits() = TemperatureUnit.C

    override fun getMeasureUnits(): Units =
        sharedPreferences.getString(KEY_MEASURE_UNITS, DEFAULT_MEASURE_UNITS)!!.let(Units::valueOf)


    override fun getServerAPIUnits() = Units.METRIC

    private fun SharedPreferences.save(action: SharedPreferences.Editor.() -> Unit) =
        Completable.fromAction { edit(true, action) }

    private fun <T> SharedPreferences.getMaybe(supplier: SharedPreferences.() -> T?) =
        Maybe.fromCallable<T> {
            supplier(this)
        }

    private fun <T> SharedPreferences.get(supplier: SharedPreferences.() -> T) =
        Single.fromCallable {
            supplier(this)
        }

    private fun <V : Any?> SharedPreferences.getValue(key: String, type: Class<V>): V? =
        sharedPreferences.getString(key, null)?.let { gson.fromJson(it, type) }

    private fun <V : Any> SharedPreferences.Editor.putValue(key: String, value: V) {
        putString(key, gson.toJson(value))
    }

    companion object {
        const val KEY_LOCALE = "Locale"
        const val KEY_MEASURE_UNITS = "Units"
        const val KEY_TEMPERATURE_UNITS = "TemperatureUnits"
        const val DEFAULT_LOCALE = "ru-RU"
        val DEFAULT_MEASURE_UNITS = Units.METRIC.name
        val DEFAULT_TEMPERATURE_UNITS = TemperatureUnit.C.name
    }
}