package com.lab.esh1n.weather.weather.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.base.BaseObserver
import com.lab.esh1n.weather.base.BaseVMFragment
import com.lab.esh1n.weather.domain.base.ErrorModel
import com.lab.esh1n.weather.utils.SnackbarBuilder
import com.lab.esh1n.weather.utils.setVisibleOrGone
import com.lab.esh1n.weather.weather.adapter.PlacesAdapter
import com.lab.esh1n.weather.weather.model.PlaceWeather
import com.lab.esh1n.weather.weather.viewmodel.AllPlacesViewModel

class AllPlacesFragment : BaseVMFragment<AllPlacesViewModel>() {

    override val viewModelClass = AllPlacesViewModel::class.java

    override val layoutResource = R.layout.fragment_all_places

    private lateinit var adapter: PlacesAdapter

    private var placeRecyclerView: RecyclerView? = null
    private var emptyView: TextView? = null

    override fun setupView(rootView: View) {
        super.setupView(rootView)
        adapter = PlacesAdapter(this::onPlaceClicked)
        placeRecyclerView = rootView.findViewById(R.id.list_places)
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
    }

    private fun onPlaceClicked(placeWeather: PlaceWeather) {
        viewModel.saveCurrentPlace(placeWeather.cityName)
    }

    companion object {
        fun newInstance() = AllPlacesFragment()
    }
}