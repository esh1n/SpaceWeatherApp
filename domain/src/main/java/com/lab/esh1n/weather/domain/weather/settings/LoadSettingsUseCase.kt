package com.lab.esh1n.weather.domain.weather.settings

import com.esh1n.core_android.error.ErrorsHandler
import com.esh1n.core_android.ui.viewmodel.Resource
import com.lab.esh1n.data.cache.AppPrefs
import com.lab.esh1n.weather.domain.weather.UseCase
import io.reactivex.Single

class LoadSettingsUseCase(private val prefs: AppPrefs, errorHandler: ErrorsHandler) : UseCase<Single<Resource<HashMap<String, Any>>>, Unit>(errorHandler) {
    override fun perform(args: Unit): Single<Resource<HashMap<String, Any>>> {
        return Single.fromCallable {
            val hashMap = HashMap<String, Any>()
            hashMap[AppPrefs.KEY_LOCALE] = prefs.getLocale()
            return@fromCallable hashMap
        }.map { Resource.success(it) }
                .onErrorReturn { error ->
                    Resource.error(errorsHandler.handle(error))
                }
    }
}