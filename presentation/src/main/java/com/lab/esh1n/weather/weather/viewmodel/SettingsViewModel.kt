package com.lab.esh1n.weather.weather.viewmodel

import android.app.Application
import com.esh1n.core_android.rx.applyAndroidSchedulers
import com.esh1n.core_android.ui.viewmodel.BaseViewModel
import com.esh1n.core_android.ui.viewmodel.Resource
import com.esh1n.core_android.ui.viewmodel.SingleLiveEvent
import com.lab.esh1n.data.cache.entity.AppSettingsInteractor
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.WeatherApp
import com.lab.esh1n.weather.domain.weather.settings.LoadSettingsUseCase
import com.lab.esh1n.weather.weather.model.HeaderSettingModel
import com.lab.esh1n.weather.weather.model.SettingsModel
import com.lab.esh1n.weather.weather.model.TextSettingModel
import java.util.*
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val loadSettingsUseCase: LoadSettingsUseCase, application: Application) : BaseViewModel(application) {


    val settings = SingleLiveEvent<Resource<List<SettingsModel>>>()

    fun loadSettings() {
        loadSettingsUseCase.perform(Unit)
                .map {
                    Resource.map<HashMap<String, Any>, List<SettingsModel>>(it) { dictionary ->
                        val settings = arrayListOf<SettingsModel>()
                        settings.add(HeaderSettingModel(getHeaderString()))
                        settings.add(TextSettingModel(getLanguageString(), (dictionary[AppSettingsInteractor.KEY_LOCALE] as Locale).displayLanguage))
                        return@map settings
                    }
                }.applyAndroidSchedulers()
                .subscribe({ result -> settings.postValue(result) },
                        {
                            settings.postValue(Resource.error(it))
                        })
                .disposeOnDestroy()
    }

    private fun getHeaderString(): String {
        return getApplication<WeatherApp>().getString(R.string.text_settings_header)
    }

    private fun getLanguageString(): String {
        return getApplication<WeatherApp>().getString(R.string.text_settings_language)
    }
}