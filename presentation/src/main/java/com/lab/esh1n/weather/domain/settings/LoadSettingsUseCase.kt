package com.lab.esh1n.weather.domain.settings

import com.esh1n.core_android.error.ErrorsHandler
import com.lab.esh1n.weather.data.cache.AppPrefs
import com.lab.esh1n.weather.domain.UseCase
import io.reactivex.Single

class LoadSettingsUseCase(private val prefs: AppPrefs, errorHandler: ErrorsHandler) :
    UseCase<Single<HashMap<String, String>>, Unit>(errorHandler) {
    override fun perform(args: Unit): Single<HashMap<String, String>> {
        return Single.fromCallable {
            HashMap<String, String>().apply {
                this[AppPrefs.KEY_LOCALE] = prefs.getLocale().displayLanguage
            }
        }
    }
}