package com.lab.esh1n.weather.weather.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
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
            MaterialTheme {
//                FavouritesFlowScreen(
//                    favsFlow = viewModel.uiState,
//                    viewModel::onItemClicked,
//                    viewModel::onFavIconItemChange
//                )
            }
        }
    }

//    @Composable
//    fun FavouritesFlowScreen(
//        favsFlow: Flow<FavouritesUiState>,
//        onItemClicked: (FavouriteUiModel) -> Unit,
//        onFavIconItemChange: (FavouriteUiModel) -> Unit
//    ) {
//        val state = favsFlow.withCurrentLifecycle().collectAsState(FavouritesUiState())
//        FavouritesScreen(
//            uiState = state.value,
//            onItemClicked = onItemClicked,
//            onFavIconItemChange = onFavIconItemChange,
//            onGoToSearchPlace = ::openSearchPlacesScreen
//        )
//    }
}