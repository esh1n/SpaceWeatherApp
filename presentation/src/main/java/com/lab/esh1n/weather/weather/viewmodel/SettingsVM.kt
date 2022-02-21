package com.lab.esh1n.weather.weather.viewmodel

import com.esh1n.core_android.common.subscribeOnError
import com.esh1n.core_android.rx.applyAndroidSchedulers
import com.esh1n.core_android.ui.viewmodel.AutoClearViewModel
import com.lab.esh1n.weather.data.cache.LanguageTag
import com.lab.esh1n.weather.domain.prefs.IPrefsInteractor
import com.worka.android.app.common.livedata.LiveDataFactory
import javax.inject.Inject

class SettingsVM @Inject constructor(private val prefsInteractor: IPrefsInteractor) :
    AutoClearViewModel() {


    val uiState = LiveDataFactory.mutable<SettingsState>()

    fun loadSettings() {
        prefsInteractor.getUserSettingsObservable()
            .map { userSettings ->
                SettingsState(
                    notificationEnabled = userSettings.notificationEnabled,
                    autoPlaceDetection = userSettings.autoPlaceDetection,
                    alertsMailing = userSettings.alertsMailing,
                    language = userSettings.language
                )
            }.applyAndroidSchedulers()
            .subscribeOnError({ newUserSettings -> updateState { newUserSettings } })
    }

    fun toggleNotificationSettings() {
        updateState { copy(notificationEnabled = this.notificationEnabled.not()) }
    }

    fun toggleAutoPlaceDetection() {
        updateState { copy(autoPlaceDetection = this.autoPlaceDetection.not()) }
    }

    fun toggleAlertsMailing() {
        updateState { copy(alertsMailing = this.alertsMailing.not()) }
    }

    fun setLanguage(language: LanguageTag) {

    }

    private fun updateState(transform: SettingsState.() -> SettingsState) {
        uiState.value = uiState.value?.transform()
    }
}

data class SettingsState(
    val notificationEnabled: Boolean,
    val autoPlaceDetection: Boolean,
    val alertsMailing: Boolean,
    val language: LanguageTag
) {
    companion object {
        fun empty() = SettingsState(
            notificationEnabled = false,
            autoPlaceDetection = false,
            alertsMailing = false,
            language = LanguageTag.Russian
        )
    }
}