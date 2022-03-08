package com.lab.esh1n.weather.weather.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lab.esh1n.weather.BuildConfig
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.weather.settings.Tags.TAG_CHECK_ITEM
import com.lab.esh1n.weather.weather.settings.Tags.TAG_MARKETING_OPTION
import com.lab.esh1n.weather.weather.settings.Tags.TAG_SELECT_THEME
import com.lab.esh1n.weather.weather.settings.Tags.TAG_THEME
import com.lab.esh1n.weather.weather.settings.Tags.TAG_THEME_OPTION
import com.lab.esh1n.weather.weather.settings.Tags.TAG_TOGGLE_ITEM
import com.lab.esh1n.weather.weather.settings.model.Theme
import com.lab.esh1n.weather.weather.viewmodel.AlertsOption
import com.lab.esh1n.weather.weather.viewmodel.SettingsState
import com.lab.esh1n.weather.weather.viewmodel.SettingsVM

@Composable
fun Settings(viewModel: SettingsVM = viewModel()) {
    val uiState: SettingsState by viewModel.uiState.collectAsState()
    MaterialTheme {
        UserSettingsScreen(
            state = uiState,
            onNotificationClicked = viewModel::toggleNotificationSettings,
            onAutoPlaceToggled = viewModel::toggleAutoPlaceDetection,
            onManageSubscription = viewModel::manageSubscription,
            onToggleAlertsMailing = viewModel::toggleAlertsMailing,
            onSetTheme = viewModel::setTheme
        )
    }
}

@Composable
fun UserSettingsScreen(
    state: SettingsState,
    onNotificationClicked: (Boolean) -> Unit,
    onAutoPlaceToggled: () -> Unit,
    onManageSubscription: () -> Unit,
    onToggleAlertsMailing: (AlertsOption) -> Unit,
    onSetTheme: (Theme) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        TopAppBar(
            backgroundColor = MaterialTheme.colors.surface,
            contentPadding = PaddingValues(start = 12.dp)
        ) {
            Icon(
                tint = MaterialTheme.colors.onSurface,
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(id = R.string.cd_go_back)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(id = R.string.menu_settings),
                fontSize = 18.sp,
                color = MaterialTheme.colors.onSurface
            )
        }
        NotificationSettings(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.text_notifications_setting),
            checked = state.notificationEnabled,
            onCheckedChanged = onNotificationClicked
        )
        Divider()
        AutoPlaceDetection(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.text_auto_place_setting),
            checked = state.autoPlaceDetection,
            onAutoPlaceToggled = onAutoPlaceToggled
        )
        Divider()
        ManageSubscriptionSettingItem(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.text_subscription_setting),
            onSettingClicked = onManageSubscription
        )
        SectionSpacer(modifier = Modifier.fillMaxWidth())
        AlertSettingItem(
            modifier = Modifier.fillMaxWidth(),
            selectedOption = state.alertsOption,
            onOptionSelected = onToggleAlertsMailing
        )
        Divider()
        ThemeSettingItem(
            modifier = Modifier.fillMaxWidth(),
            selectedTheme = state.themeOption,
            onThemeSelected = onSetTheme
        )
        SectionSpacer(modifier = Modifier.fillMaxWidth())
        AppVersionSettingItem(appVersion = "${BuildConfig.VERSION_CODE}")
    }
}

@Composable
fun SettingItem(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(modifier = modifier.heightIn(min = 56.dp)) {
        content()
    }
}

@Composable
fun ThemeSettingItem(
    modifier: Modifier = Modifier,
    selectedTheme: Theme,
    onThemeSelected: (theme: Theme) -> Unit
) {
    SettingItem(modifier = modifier) {
        var expanded by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier
                .clickable(
                    onClickLabel = stringResource(id = R.string.cd_select_theme)
                ) {
                    expanded = !expanded
                }
                .padding(16.dp)
                .testTag(TAG_SELECT_THEME),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.settings_options_theme)
            )
            Text(
                modifier = Modifier.testTag(TAG_THEME),
                text = stringResource(id = selectedTheme.label)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            offset = DpOffset(16.dp, 0.dp)
        ) {
            Theme.values().forEach { theme ->
                val themeLabel = stringResource(id = theme.label)
                DropdownMenuItem(
                    modifier = Modifier.testTag(TAG_THEME_OPTION + themeLabel),
                    onClick = {
                        onThemeSelected(theme)
                        expanded = false
                    }) {
                    Text(text = themeLabel)
                }
            }
        }
    }
}

@Composable
fun AlertSettingItem(
    modifier: Modifier = Modifier,
    selectedOption: AlertsOption,
    onOptionSelected: (AlertsOption) -> Unit
) {
    val options = listOf(
        stringResource(id = R.string.settings_options_alert_allowed),
        stringResource(id = R.string.settings_options_alert_not_allowed)
    )
    SettingItem(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = stringResource(id = R.string.settings_options_alert))
            Spacer(modifier = Modifier.height(8.dp))
            options.forEachIndexed { index, option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(selected = selectedOption.id == index, onClick = {
                            val alertsOption =
                                if (index == AlertsOption.ALLOWED.id) AlertsOption.ALLOWED else AlertsOption.NOT_ALLOWED
                            onOptionSelected(alertsOption)
                        }, role = Role.RadioButton)
                        .padding(10.dp)
                        .testTag(TAG_MARKETING_OPTION + index),
                ) {
                    RadioButton(selected = selectedOption.id == index, onClick = null)
                    Text(modifier = Modifier.padding(start = 18.dp), text = option)
                }
            }
        }
    }
}

@Composable
fun ManageSubscriptionSettingItem(
    modifier: Modifier = Modifier,
    title: String,
    onSettingClicked: () -> Unit
) {
    SettingItem(modifier = modifier) {
        Row(modifier = Modifier
            .clickable(onClickLabel = stringResource(id = R.string.text_open_subscription)) { onSettingClicked() }
            .padding(16.dp)) {
            Text(modifier = Modifier.weight(1f), text = title)
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = stringResource(
                    id = R.string.text_open_subscription
                )
            )
        }
    }
}

@Composable
fun NotificationSettings(
    modifier: Modifier = Modifier,
    title: String,
    checked: Boolean,
    onCheckedChanged: (checked: Boolean) -> Unit
) {
    val notificationsEnabledState = if (checked) {
        stringResource(id = R.string.cd_notifications_enabled)
    } else stringResource(id = R.string.cd_notifications_disabled)
    SettingItem(modifier = modifier) {
        Row(
            modifier = Modifier
                .toggleable(
                    value = checked,
                    onValueChange = onCheckedChanged,
                    role = Role.Switch
                )
                .semantics {
                    stateDescription = notificationsEnabledState
                }
                .padding(horizontal = 16.dp)
                .testTag(TAG_TOGGLE_ITEM),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = title
            )
            Switch(checked = checked, onCheckedChange = null)
        }
    }
}

@Composable
fun AutoPlaceDetection(
    modifier: Modifier = Modifier,
    title: String,
    checked: Boolean,
    onAutoPlaceToggled: () -> Unit
) {
    val semanticsDescription =
        stringResource(id = if (checked) R.string.cd_auto_place_enabled else R.string.cd_auto_place_disabled)
    SettingItem(modifier = modifier) {
        Row(
            modifier = Modifier
                .toggleable(
                    value = checked,
                    onValueChange = { onAutoPlaceToggled() },
                    role = Role.Checkbox
                )
                .semantics { stateDescription = semanticsDescription }
                .padding(horizontal = 16.dp)
                .testTag(TAG_CHECK_ITEM),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, modifier = Modifier.weight(1f))
            Checkbox(checked = checked, onCheckedChange = null)
        }
    }
}

@Composable
fun AppVersionSettingItem(modifier: Modifier = Modifier, appVersion: String) {
    SettingItem(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .semantics(mergeDescendants = true) { },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.setting_app_version)
            )
            Text(text = appVersion)
        }
    }
}