package com.lab.esh1n.weather.weather.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
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
            val state = viewModel.uiState.observeAsState(initial = SettingsState.empty())
            MaterialTheme {
                UserSettingsScreen(
                    settingsState = state.value,
                    onNotificationClicked = viewModel::toggleNotificationSettings,
                    onMailingAlertClicked = viewModel::toggleAlertsMailing
                )
            }
        }
    }

    @Composable
    fun UserSettingsScreen(
        settingsState: SettingsState,
        onNotificationClicked: () -> Unit,
        onMailingAlertClicked: () -> Unit,
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
                onToggleNotifications = onNotificationClicked
            )
            Divider()

        }
    }

    @Composable
    fun NotificationSettings(
        title: String,
        modifier: Modifier = Modifier,
        checked: Boolean,
        onToggleNotifications: () -> Unit
    ) {
        val semanticsDescription =
            stringResource(id = if (checked) R.string.cd_notifications_enabled else R.string.cd_notifications_disabled)
        Surface(modifier = modifier) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .toggleable(
                        value = checked,
                        role = Role.Switch,
                        onValueChange = { onToggleNotifications() }
                    )
                    .semantics {
                        stateDescription = semanticsDescription
                    }
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = title, modifier = Modifier.weight(1f))
            }
            //
            Switch(checked = checked, onCheckedChange = null)
        }
    }

}