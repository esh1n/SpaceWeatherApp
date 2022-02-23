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


    val uiState = LiveDataFactory.mutable<SettingsState>(SettingsState.empty())

    fun loadSettings() {
        prefsInteractor.getUserSettingsObservable()
            .map { userSettings ->
                SettingsState(
                    notificationEnabled = userSettings.notificationEnabled,
                    autoPlaceDetection = userSettings.autoPlaceDetection,
                    alertsOption = if (userSettings.alertsMailing) AlertsOption.ALLOWED else AlertsOption.NOT_ALLOWED,
                    language = userSettings.language
                )
            }.applyAndroidSchedulers()
            .subscribeOnError({ newUserSettings -> updateState { newUserSettings } })
    }

    fun toggleNotificationSettings(notificationEnabled: Boolean) {
        updateState { copy(notificationEnabled = notificationEnabled) }
    }

    fun toggleAutoPlaceDetection() {
        updateState { copy(autoPlaceDetection = this.autoPlaceDetection.not()) }
    }

    fun toggleAlertsMailing(option: AlertsOption) {
        updateState { copy(alertsOption = option) }
    }

    fun manageSubscription() {}


    fun setLanguage(language: LanguageTag) {

    }

    private fun updateState(transform: SettingsState.() -> SettingsState) {
        uiState.value = uiState.value?.transform()
    }
}

enum class AlertsOption(val id: Int) {
    ALLOWED(0), NOT_ALLOWED(1)
}

data class SettingsState(
    val notificationEnabled: Boolean,
    val autoPlaceDetection: Boolean,
    val alertsOption: AlertsOption,
    val language: LanguageTag
) {
    companion object {
        fun empty() = SettingsState(
            notificationEnabled = false,
            autoPlaceDetection = false,
            alertsOption = AlertsOption.ALLOWED,
            language = LanguageTag.Russian
        )
    }
}