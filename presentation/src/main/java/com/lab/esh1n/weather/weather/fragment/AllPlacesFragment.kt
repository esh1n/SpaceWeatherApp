package com.lab.esh1n.weather.weather.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crashlytics.android.Crashlytics
import com.esh1n.core_android.error.ErrorModel
import com.esh1n.core_android.ui.addFragmentToStack
import com.esh1n.core_android.ui.fragment.BaseVMFragment
import com.esh1n.core_android.ui.viewmodel.BaseObserver
import com.esh1n.utils_android.ui.DialogUtil
import com.esh1n.utils_android.ui.SnackbarBuilder
import com.esh1n.utils_android.ui.setVisibleOrGone
import com.lab.esh1n.data.cache.entity.WeatherWithPlace
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.weather.adapter.PlacesAdapter
import com.lab.esh1n.weather.weather.model.PlaceModel
import com.lab.esh1n.weather.weather.viewmodel.AllPlacesVM

class AllPlacesFragment : BaseVMFragment<AllPlacesVM>() {

    override val viewModelClass = AllPlacesVM::class.java

    override val layoutResource = R.layout.fragment_all_places

    private lateinit var adapter: PlacesAdapter

    private var placeRecyclerView: RecyclerView? = null
    private var emptyView: TextView? = null
    private var loadingIndicator: View? = null

    override fun setupView(rootView: View, savedInstanceState: Bundle?) {
        super.setupView(rootView, savedInstanceState)
        adapter = PlacesAdapter(iPlaceClickable)
        placeRecyclerView = rootView.findViewById(R.id.list_places)
        loadingIndicator = rootView.findViewById(R.id.loading_indicator)
        emptyView = rootView.findViewById(R.id.tv_no_places)
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
        viewModel.allCities.observe(this, object : BaseObserver<List<PlaceModel>>() {
            override fun onError(error: ErrorModel?) {
                SnackbarBuilder.buildSnack(view!!, error?.message ?: "").show()
            }

            override fun onData(data: List<PlaceModel>?) {
                val isEmpty = data?.isEmpty() ?: true
                emptyView!!.setVisibleOrGone(isEmpty)
                adapter.swapCities(data.orEmpty())
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
        viewModel.loadPlaces()
    }


    private val iPlaceClickable = object : PlacesAdapter.IPlaceClickable {
        override fun onPlaceClick(placeWeather: PlaceModel) {
            Crashlytics.log(Log.DEBUG, "tag", "opened forecast for${placeWeather.name}")
            parentFragment?.fragmentManager.addFragmentToStack(ForecastFragment.newInstance())

        }

        override fun onPlaceOptions(placeWeather: PlaceModel) {
            DialogUtil.buildListDialog(requireActivity(),
                    getString(R.string.text_choose_action),
                    R.array.choose_place_actions) { result ->
                if (result == 0) {
                    viewModel.saveCurrentPlace(placeWeather.id)
                }
            }.show()
        }

    }

    companion object {
        fun newInstance() = AllPlacesFragment()
    }
}