package com.lab.esh1n.weather.weather.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.weather.viewmodel.AlertsOption
import com.lab.esh1n.weather.weather.viewmodel.SettingsState
import com.lab.esh1n.weather.weather.viewmodel.SettingsVM
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class SettingsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<SettingsVM>(factoryProducer = { viewModelFactory })

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

        setContent {
            val uiState: SettingsState by viewModel.uiState.observeAsState(initial = SettingsState.empty())
            MaterialTheme {
                UserSettingsScreen(
                    settingsState = uiState,
                    onNotificationClicked = viewModel::toggleNotificationSettings,
                    onAutoPlaceToggled = viewModel::toggleAutoPlaceDetection
                )
            }
        }
    }

    @Composable
    fun UserSettingsScreen(
        settingsState: SettingsState,
        onNotificationClicked: (Boolean) -> Unit,
        onAutoPlaceToggled: () -> Unit,
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
                checked = settingsState.notificationEnabled,
                onCheckedChanged = onNotificationClicked
            )
            Divider()
            AutoPlaceDetection(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.text_auto_place_setting),
                checked = settingsState.autoPlaceDetection,
                onAutoPlaceToggled = onAutoPlaceToggled
            )
            Divider()
            ManageSubscriptionSettingItem(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.text_subscription_setting),
                onSettingClicked = viewModel::manageSubscription
            )
            SectionSpacer(modifier = Modifier.fillMaxWidth())
            AlertSettingItem(
                modifier = Modifier.fillMaxWidth(),
                selectedOption = settingsState.alertsOption,
                onOptionSelected = viewModel::toggleAlertsMailing
            )
            Divider()
        }
    }

    @Composable
    fun SettingItem(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
        Surface(modifier = modifier.heightIn(min = 56.dp)) {
            content()
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
                //.testTag(TAG_TOGGLE_ITEM),
                ,
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
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, modifier = Modifier.weight(1f))
                Checkbox(checked = checked, onCheckedChange = null)
            }
        }
    }

}