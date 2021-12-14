package com.lab.esh1n.weather.weather.favourite

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
import androidx.lifecycle.flowWithLifecycle
import com.esh1n.core_android.ui.addFragmentToStack
import com.lab.esh1n.weather.weather.fragment.SearchPlacesFragment
import kotlinx.coroutines.flow.Flow


class FavouritePlacesFragment : Fragment() {

    private var composeView: ComposeView? = null

    private val viewModel by viewModels<FavouriteViewModel>()

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
        favsFlow: Flow<List<TodoItem>>,
        onItemClicked: (TodoItem) -> Unit,
        onFavIconItemChange: (TodoItem) -> Unit
    ) {
        val lifecycleOwner = LocalLifecycleOwner.current
        val favsFlowLifecycleAware = remember(favsFlow, lifecycleOwner) {
            favsFlow.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
        }

        val items by favsFlowLifecycleAware.collectAsState(emptyList())
        FavouritesScreen(
            items = items,
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

