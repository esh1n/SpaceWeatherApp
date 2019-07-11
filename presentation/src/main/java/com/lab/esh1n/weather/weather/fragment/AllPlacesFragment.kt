package com.lab.esh1n.weather.weather.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esh1n.core_android.error.ErrorModel
import com.esh1n.core_android.ui.fragment.BaseVMFragment
import com.esh1n.core_android.ui.viewmodel.BaseObserver
import com.esh1n.utils_android.ui.SnackbarBuilder
import com.esh1n.utils_android.ui.setVisibleOrGone
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.weather.WeatherActivity
import com.lab.esh1n.weather.weather.adapter.PlacesAdapter
import com.lab.esh1n.weather.weather.model.PlaceWeather
import com.lab.esh1n.weather.weather.viewmodel.AllPlacesViewModel

class AllPlacesFragment : BaseVMFragment<AllPlacesViewModel>() {

    override val viewModelClass = AllPlacesViewModel::class.java

    override val layoutResource = R.layout.fragment_all_places

    private lateinit var adapter: PlacesAdapter

    private var placeRecyclerView: RecyclerView? = null
    private var emptyView: TextView? = null
    private var loadingIndicator: View? = null

    override fun setupView(rootView: View) {
        super.setupView(rootView)
        adapter = PlacesAdapter(this::onPlaceClicked)
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
        viewModel.allCities.observe(this, object : BaseObserver<List<PlaceWeather>>() {
            override fun onError(error: ErrorModel?) {
                SnackbarBuilder.buildSnack(view!!, error?.message ?: "").show()
            }

            override fun onData(data: List<PlaceWeather>?) {
                val isEmpty = data?.isEmpty() ?: true
                emptyView!!.setVisibleOrGone(isEmpty)
                adapter.swapCities(data.orEmpty())
            }

        })

        viewModel.updateCurrentPlaceOperation.observe(this, object : BaseObserver<Unit>() {
            override fun onData(data: Unit?) {

                (requireActivity() as WeatherActivity).setCurrentWeather()

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

    private fun onPlaceClicked(placeWeather: PlaceWeather) {
        viewModel.saveCurrentPlace(placeWeather.cityName)
    }

    companion object {
        fun newInstance() = AllPlacesFragment()
    }
}