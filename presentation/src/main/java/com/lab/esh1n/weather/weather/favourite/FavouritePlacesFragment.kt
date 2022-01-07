package com.lab.esh1n.weather.weather.favourite

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import com.esh1n.core_android.ui.addFragmentToStack
import com.lab.esh1n.weather.weather.fragment.SearchPlacesFragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class FavouritePlacesFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var composeView: ComposeView? = null

    private val viewModel by viewModels<FavouriteVM>(factoryProducer = { viewModelFactory })

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
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
            composeView = this
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
        val lifecycleOwner = LocalLifecycleOwner.current
        val favsFlowLifecycleAware = remember(favsFlow, lifecycleOwner) {
            favsFlow.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
        }

        val uiState by favsFlowLifecycleAware.collectAsState(FavouritesUiState())
        FavouritesScreen(
            uiState = uiState,
            onItemClicked = onItemClicked,
            onFavIconItemChange = onFavIconItemChange,
            onGoToSearchPlace = ::openSearchPlacesScreen
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }
}

