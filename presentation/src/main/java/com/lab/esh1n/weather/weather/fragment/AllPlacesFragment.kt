package com.lab.esh1n.weather.weather.fragment

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
import com.lab.esh1n.data.cache.entity.PlaceWithCurrentWeatherEntry
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.weather.adapter.PlacesAdapter
import com.lab.esh1n.weather.weather.viewmodel.AllPlacesVM
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class AllPlacesFragment : BaseVMFragment<AllPlacesVM>() {

    override val viewModelClass = AllPlacesVM::class.java

    override val layoutResource = R.layout.fragment_recyclerview_with_progress

    private lateinit var adapter: PlacesAdapter

    private var placeRecyclerView: RecyclerView? = null
    private var emptyView: TextView? = null
    private var loadingIndicator: View? = null

    private val emptySearchTextId: Int
        @StringRes
        get() = R.string.empty_text_search

    private var searchView: SearchView? = null


    private val fullEmptySearchDescription: String
        get() = getString(emptySearchTextId, searchView?.query)

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
        emptyView = rootView.findViewById(R.id.tv_no_items)
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.allCities.observe(this, object : BaseObserver<PagedList<PlaceWithCurrentWeatherEntry>>() {
            override fun onError(error: ErrorModel?) {
                SnackbarBuilder.buildSnack(view!!, error?.message ?: "").show()
            }

            override fun onData(data: PagedList<PlaceWithCurrentWeatherEntry>?) {
                val isEmpty = data?.isEmpty() ?: true
                emptyView!!.setVisibleOrGone(isEmpty)
                adapter.submitList(data)
            }

        })

        viewModel.updateCurrentPlaceOperation.observe(this, object : BaseObserver<WeatherWithPlace>() {
            override fun onData(data: WeatherWithPlace?) {

                (parentFragment as WeatherHostFragment).setCurrentWeather()

            }

            override fun onError(error: ErrorModel?) {
                SnackbarBuilder.buildErrorSnack(view!!, getString(R.string.error_unexpected)).show()
            }

            override fun onProgress(progress: Boolean) {
                super.onProgress(progress)
                loadingIndicator?.setVisibleOrGone(progress)
            }

        })

    }

    override fun onStart() {
        super.onStart()
        load()
    }

    private fun load() {
        val query = searchView?.query ?: ""
        viewModel.searchPlaces(Observable.just(query.toString()))
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        val searchItem: MenuItem? = menu.findItem(R.id.action_search_tag)

        if (searchItem != null) {
            // searchItem.expandActionView()
            //searchItem.actionView.clearFocus()
            searchView = searchItem.actionView as SearchView
            searchView?.setOnQueryTextListener(null)
            val hint = getString(queryHintResourceId())
            searchView?.queryHint = hint
            searchView?.onActionViewExpanded()
            searchView?.let { sv ->


                val searchSource = sv.queryTextChanges()
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
            parentFragment?.parentFragmentManager.addFragmentToStack(ForecastFragment.newInstance(placeId, placeName))

        }

        override fun onPlaceOptions(placeId: Int) {
            DialogUtil.buildListDialog(requireActivity(),
                    getString(R.string.text_choose_action),
                    R.array.choose_place_actions) { result ->
                if (result == 0) {
                    viewModel.saveCurrentPlace(placeId)
                }
            }.show()
        }

    }

    companion object {
        fun newInstance() = AllPlacesFragment()
    }
}