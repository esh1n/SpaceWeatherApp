package com.lab.esh1n.weather.presentation.favourite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.esh1n.core_android.ui.addFragmentToStack
import com.lab.esh1n.weather.presentation.fragment.SearchPlacesFragment
import com.lab.esh1n.weather.utils.withCurrentLifecycle
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class FavouritePlacesFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<FavouriteVM>(factoryProducer = { viewModelFactory })

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
                FavouritesFlowScreen(
                    favsFlow = viewModel.uiState,
                    viewModel::onItemClicked,
                    viewModel::onFavIconItemChange
                )
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    private fun openSearchPlacesScreen() {
        parentFragment?.parentFragmentManager.addFragmentToStack(SearchPlacesFragment.newInstance())
    }


    @Composable
    fun FavouritesFlowScreen(
        favsFlow: Flow<FavouritesUiState>,
        onItemClicked: (FavouriteUiModel) -> Unit,
        onFavIconItemChange: (FavouriteUiModel) -> Unit
    ) {
        val uiState by favsFlow.withCurrentLifecycle().collectAsState(FavouritesUiState())
        FavouritesScreen(
            uiState = uiState,
            onItemClicked = onItemClicked,
            onFavIconItemChange = onFavIconItemChange,
            onGoToSearchPlace = ::openSearchPlacesScreen
        )
    }
}

