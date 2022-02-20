package com.lab.esh1n.weather.data.cache

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.lab.esh1n.weather.domain.prefs.IPrefsInteractor
import com.lab.esh1n.weather.domain.prefs.TemperatureUnit
import com.lab.esh1n.weather.domain.prefs.Units
import com.lab.esh1n.weather.domain.prefs.UserSettings
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*

class AppPrefs(private val sharedPreferences: SharedPreferences) : IPrefsInteractor {
    //TODO move passing gson from DI
    private val gson: Gson = Gson()

    private val prefsObservable = Observable.create<Pair<SharedPreferences, String>> { emitter ->
        val listener = { prefs: SharedPreferences, key: String -> emitter.onNext(prefs to key) }
        emitter.setCancellable {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }.share()

    fun <V : Any> getSingleKeySource(
        key: String,
        type: Class<V>,
        nullValueReplacement: V
    ): Observable<V> {
        fun getValue(key: String, type: Class<V>, nullValueReplacement: V) =
            getValue(key, type) ?: nullValueReplacement
        return getMultipleKeysSource(listOf(key), keyMapper = {
            getValue(
                key = key,
                type = type,
                nullValueReplacement
            )
        })
    }

    private fun <V : Any> getMultipleKeysSource(
        keys: List<String>,
        keyMapper: () -> V
    ): Observable<V> {
        return prefsObservable
            .filter { (_, subjectKey) -> subjectKey in keys }
            .map { (_, _) -> keyMapper() }
            .startWith(keyMapper())
    }

    private fun <V> getValue(key: String, type: Class<V>): V? {
        val json = sharedPreferences.getString(key, null)
        return json?.let { gson.fromJson(json, type) }
    }

    override fun getLocale(): Single<Locale> {
        return sharedPreferences.get { retrieveLocale() }
    }

    private fun SharedPreferences.retrieveLocale(): Locale =
        (getString(KEY_LOCALE, DEFAULT_LOCALE) ?: DEFAULT_LOCALE).let(Locale::forLanguageTag)

    override fun getLanguage(): Single<String> = getLocale().map { it.language }

    override fun getLocaleBlocking(): Locale = sharedPreferences.retrieveLocale()

    override fun getAppTemperatureUnits(): TemperatureUnit {
        return sharedPreferences.getString(KEY_TEMPERATURE_UNITS, DEFAULT_TEMPERATURE_UNITS)!!
            .let(TemperatureUnit::valueOf)
    }

    override fun getServerAPITemperatureUnits() = TemperatureUnit.C

    override fun getUserSettingsObservable(): Observable<UserSettings> {
        fun getUserSettings(): UserSettings {
            val locale = getLocaleBlocking()
            return UserSettings(
                notificationEnabled = false,
                autoPlaceDetection = false,
                alertsMailing = false,
                language = locale
            )
        }
        return getMultipleKeysSource(listOf(KEY_MEASURE_UNITS), ::getUserSettings)
    }

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