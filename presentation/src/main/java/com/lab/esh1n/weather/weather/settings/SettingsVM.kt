package com.lab.esh1n.weather.weather.settings

import com.esh1n.core_android.common.subscribeOnError
import com.esh1n.core_android.rx.applyAndroidSchedulers
import com.esh1n.core_android.ui.viewmodel.AutoClearViewModel
import com.lab.esh1n.weather.BuildConfig
import com.lab.esh1n.weather.data.cache.LanguageTag
import com.lab.esh1n.weather.domain.prefs.IPrefsInteractor
import com.lab.esh1n.weather.weather.settings.model.Theme
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class SettingsVM @Inject constructor(private val prefsInteractor: IPrefsInteractor) :
    AutoClearViewModel() {

    init {
        loadSettings()
    }

    val uiState =
        MutableStateFlow(getEmptySettingsState())//LiveDataFactory.mutable(getEmptySettingsState())

    private fun loadSettings() {
        prefsInteractor.getUserSettingsObservable()
            .map { userSettings ->
                SettingsState(
                    notificationEnabled = userSettings.notificationEnabled,
                    autoPlaceDetection = userSettings.autoPlaceDetection,
                    alertsOption = if (userSettings.alertsMailing) AlertsOption.ALLOWED else AlertsOption.NOT_ALLOWED,
                    language = userSettings.language,
                    themeOption = Theme.SYSTEM,
                    getAppVersion()
                )
            }
            .applyAndroidSchedulers()
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

    fun setTheme(option: Theme) {
        updateState { copy(themeOption = option) }
    }

    fun manageSubscription() {}


    fun setLanguage(languageTag: LanguageTag) {
        prefsInteractor.updateLanguage(languageTag)
            .applyAndroidSchedulers()
            .subscribeOnError({
                updateState { copy(language = languageTag) }
            })
    }

    private fun updateState(transform: SettingsState.() -> SettingsState) {
        uiState.value = uiState.value.transform()
    }

    private fun getEmptySettingsState() =
        SettingsState(
            notificationEnabled = false,
            autoPlaceDetection = false,
            alertsOption = AlertsOption.ALLOWED,
            language = LanguageTag.Russian,
            themeOption = Theme.SYSTEM,
            getAppVersion()
        )

    companion object {
        fun getAppVersion() = "${BuildConfig.VERSION_NAME}: ${BuildConfig.VERSION_CODE}"
    }

}

enum class AlertsOption(val id: Int) {
    ALLOWED(0), NOT_ALLOWED(1)
}

data class SettingsState(
    val notificationEnabled: Boolean,
    val autoPlaceDetection: Boolean,
    val alertsOption: AlertsOption,
    val language: LanguageTag,
    val themeOption: Theme,
    val appVersion: String
)