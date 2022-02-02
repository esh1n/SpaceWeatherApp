package com.lab.esh1n.weather.weather.viewmodel

import com.esh1n.core_android.rx.applyAndroidSchedulers
import com.esh1n.core_android.ui.BaseVM
import com.esh1n.core_android.ui.viewmodel.SingleLiveEvent
import com.lab.esh1n.weather.data.cache.AppPrefs
import com.lab.esh1n.weather.domain.settings.LoadSettingsUseCase
import com.lab.esh1n.weather.weather.model.HeaderSettingModel
import com.lab.esh1n.weather.weather.model.SettingsModel
import com.lab.esh1n.weather.weather.model.TextSettingModel
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val loadSettingsUseCase: LoadSettingsUseCase) :
    BaseVM() {


    val settings = SingleLiveEvent<List<SettingsModel>>()

    fun loadSettings() {
        loadSettingsUseCase.perform(Unit)
            .map { dictionary ->
                arrayListOf<SettingsModel>().apply {
                    add(HeaderSettingModel(KEY_MAIN_HEADER))
                    val locale = dictionary[AppPrefs.KEY_LOCALE]
                    locale?.let { add(TextSettingModel(KEY_LANGUAGE, it)) }
                }
            }.applyAndroidSchedulers()
            .subscribe(settings::postValue)
            .disposeOnDestroy()
    }


    companion object {
        const val KEY_MAIN_HEADER = "KEY_MAIN_HEADER"
        const val KEY_LANGUAGE = "KEY_LANGUAGE"
    }
}