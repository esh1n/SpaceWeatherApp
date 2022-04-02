package com.lab.esh1n.weather.domain.prefs

import com.lab.esh1n.weather.data.cache.LanguageTag
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*

interface IPrefsInteractor {
    fun getMeasureUnits(): Units
    fun getServerAPIUnits(): Units
    fun getLocale(): Single<Locale>
    fun getServerLanguage(): Single<String>
    fun getLocaleBlocking(): Locale
    fun getAppTemperatureUnits(): TemperatureUnit
    fun getServerAPITemperatureUnits(): TemperatureUnit
    fun getUserSettingsObservable(): Observable<UserSettings>
    fun getUserSelectedLanguageUpdates(): Observable<LanguageTag>
    fun updateLanguage(tag: LanguageTag): Completable
}

enum class Units(val serverValue: String) {
    METRIC("metric"), IMPERIAL("imperial");
}

enum class TemperatureUnit {
    F, C;

    companion object {
        fun getBySystem(units: Units): TemperatureUnit {
            return if (units == Units.METRIC) {
                C
            } else {
                F
            }
        }
    }
}


