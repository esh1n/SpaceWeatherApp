package com.lab.esh1n.weather

import androidx.annotation.StringRes
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.lab.esh1n.weather.presentation.settings.Settings
import org.junit.Rule
import org.junit.Test

class SettingsTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun Enable_Notificans_Settings_Is_Displayed() {
        assertSettingIsDisplayed(R.string.text_notifications_setting)
    }

    @Test
    fun Show_Hints_Setting_Is_Displayed() {
        assertSettingIsDisplayed(R.string.text_auto_place_setting)
    }

    @Test
    fun Manage_Subscription_Setting_Is_Displayed() {
        assertSettingIsDisplayed(R.string.text_subscription_setting)
    }

    @Test
    fun Marketing_Setting_Is_Displayed() {
        assertSettingIsDisplayed(R.string.settings_options_alert)
    }

    @Test
    fun Theme_Setting_Is_Displayed() {
        assertSettingIsDisplayed(R.string.settings_options_theme)
    }

    @Test
    fun App_Version_Setting_Is_Displayed() {
        assertSettingIsDisplayed(R.string.setting_app_version)
    }

    private fun assertSettingIsDisplayed(@StringRes title: Int) {
        composeTestRule.setContent {
            Settings()
        }
        composeTestRule.onNodeWithText(
            InstrumentationRegistry.getInstrumentation().context.getString(
                title
            )
        ).assertIsDisplayed()
    }
}
