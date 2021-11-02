package com.lab.esh1n.weather.weather.viewmodel

import android.app.Application
import com.esh1n.core_android.rx.applyAndroidSchedulers
import com.esh1n.core_android.ui.viewmodel.BaseAndroidViewModel
import com.esh1n.core_android.ui.viewmodel.Resource
import com.esh1n.core_android.ui.viewmodel.SingleLiveEvent
import com.lab.esh1n.weather.data.cache.AppPrefs
import com.lab.esh1n.weather.domain.settings.LoadSettingsUseCase
import com.lab.esh1n.weather.weather.model.HeaderSettingModel
import com.lab.esh1n.weather.weather.model.SettingsModel
import com.lab.esh1n.weather.weather.model.TextSettingModel
import java.util.*
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val loadSettingsUseCase: LoadSettingsUseCase, application: Application) : BaseAndroidViewModel(application) {


    val settings = SingleLiveEvent<Resource<List<SettingsModel>>>()

    fun loadSettings() {
        loadSettingsUseCase.perform(Unit)
                .map {
                    Resource.map<HashMap<String, Any>, List<SettingsModel>>(it) { dictionary ->
                        val settings = arrayListOf<SettingsModel>()
                        settings.add(HeaderSettingModel(KEY_MAIN_HEADER))
                        settings.add(TextSettingModel(KEY_LANGUAGE, (dictionary[AppPrefs.KEY_LOCALE] as Locale).displayLanguage))
                        settings
                    }
                }.applyAndroidSchedulers()
                .subscribe({ result -> settings.postValue(result) },
                        {
                            settings.postValue(Resource.error(it))
                        })
                .disposeOnDestroy()
    }


    companion object {
        const val KEY_MAIN_HEADER = "KEY_MAIN_HEADER"
        const val KEY_LANGUAGE = "KEY_LANGUAGE"
    }
}