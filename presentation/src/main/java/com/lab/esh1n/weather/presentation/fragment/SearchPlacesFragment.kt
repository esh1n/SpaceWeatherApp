package com.lab.esh1n.weather.presentation.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.widget.SearchView
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esh1n.core_android.error.ErrorModel
import com.esh1n.core_android.ui.addFragmentToStack
import com.esh1n.core_android.ui.fragment.BaseVMFragment
import com.esh1n.core_android.ui.viewmodel.BaseObserver
import com.esh1n.utils_android.ui.DialogUtil
import com.esh1n.utils_android.ui.SnackbarBuilder
import com.esh1n.utils_android.ui.setVisibleOrGone
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.data.cache.entity.PlaceWithCurrentWeatherEntry
import com.lab.esh1n.weather.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.presentation.adapter.PlacesAdapter
import com.lab.esh1n.weather.presentation.viewmodel.AllPlacesVM
import java.util.concurrent.TimeUnit

class SearchPlacesFragment : BaseVMFragment<AllPlacesVM>() {

    override val viewModelClass = AllPlacesVM::class.java

    override val layoutResource = R.layout.fragment_recyclerview_with_progress

    private lateinit var adapter: PlacesAdapter

    private var placeRecyclerView: RecyclerView? = null
    private var emptyView: TextView? = null
    private var loadingIndicator: View? = null

    private var searchView: SearchView? = null

    private val fullEmptySearchDescription: String
        get() {
            return if (searchView?.query.isNullOrBlank()) {
                getString(R.string.text_please_start_searching)
            } else {
                getString(R.string.empty_text_search, searchView?.query)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.action_search_tag, menu)
    }

    override fun setupView(rootView: View, savedInstanceState: Bundle?) {
        super.setupView(rootView, savedInstanceState)
        adapter = PlacesAdapter(iPlaceClickable, viewModel::placeWeatherMapper)
        placeRecyclerView = rootView.findViewById(R.id.recycler_view)
        loadingIndicator = rootView.findViewById(R.id.loading_indicator)
        emptyView = rootView.findViewById<TextView>(R.id.tv_no_items).apply {
            text = getString(R.string.search_repository_no_results_message)
        }
        placeRecyclerView?.let {
            it.layoutManager = LinearLayoutManager(requireActivity())
            it.addItemDecoration(
                DividerItemDecoration(
                    requireActivity(),
                    DividerItemDecoration.VERTICAL
                )
            )

            it.setHasFixedSize(true)
            it.adapter = adapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.allCities.observe(
            viewLifecycleOwner,
            object : BaseObserver<PagedList<PlaceWithCurrentWeatherEntry>>() {
                override fun onError(error: ErrorModel?) {
                    SnackbarBuilder.buildSnack(view, error?.message ?: "").show()
                }

                override fun onData(data: PagedList<PlaceWithCurrentWeatherEntry>?) {
                    val isEmpty = data?.isEmpty() ?: true

                    emptyView?.let {
                        it.setVisibleOrGone(isEmpty)
                        it.text = fullEmptySearchDescription
                    }
                    adapter.submitList(data)
                }

            })

        viewModel.updateCurrentPlaceOperation.observe(
            this,
            object : BaseObserver<WeatherWithPlace>() {
                override fun onData(data: WeatherWithPlace?) {
                    (parentFragment as WeatherHostFragment).setCurrentWeather()
                }

                override fun onError(error: ErrorModel?) {
                    SnackbarBuilder.buildErrorSnack(view, getString(R.string.error_unexpected))
                        .show()
                }

                override fun onProgress(progress: Boolean) {
                    super.onProgress(progress)
                    loadingIndicator?.setVisibleOrGone(progress)
                }

            })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        val searchItem: MenuItem? = menu.findItem(R.id.action_search_tag)

        if (searchItem != null) {
            searchView = (searchItem.actionView as SearchView).apply {
                setOnQueryTextListener(null)
                val hint = getString(queryHintResourceId())
                queryHint = hint
                searchItem.expandActionView();
                onActionViewExpanded()
                val searchSource = queryTextChanges()
                    .skipInitialValue()
                    .debounce(200, TimeUnit.MILLISECONDS)
                    .map { text -> text.toString() }
                    .distinctUntilChanged()

                viewModel.searchPlaces(searchSource)
            }
        }

    }

    @StringRes
    private fun queryHintResourceId() = R.string.action_search_tag

    private val iPlaceClickable = object : PlacesAdapter.IPlaceClickable {
        override fun onPlaceClick(placeId: Int, placeName: String) {
            FirebaseCrashlytics.getInstance().log("opened forecast for${placeId}")
            parentFragmentManager.addFragmentToStack(
                ForecastFragment.newInstance(
                    placeId,
                    placeName
                )
            )
        }

        override fun onPlaceOptions(placeId: Int) {
            DialogUtil.buildListDialog(
                requireActivity(),
                getString(R.string.text_choose_action),
                R.array.choose_place_actions
            ) { result ->
                if (result == 0) {
                    viewModel.saveCurrentPlace(placeId)
                }
            }.show()
        }
    }

    companion object {
        fun newInstance() = SearchPlacesFragment()
    }
}