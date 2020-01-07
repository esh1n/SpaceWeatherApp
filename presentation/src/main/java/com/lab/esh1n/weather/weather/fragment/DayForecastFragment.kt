package com.lab.esh1n.weather.weather.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esh1n.core_android.error.ErrorModel
import com.esh1n.core_android.ui.fragment.BaseVMFragment
import com.esh1n.core_android.ui.viewmodel.BaseObserver
import com.esh1n.utils_android.ui.SnackbarBuilder
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.domain.weather.weather.usecases.PlaceDayArgs
import com.lab.esh1n.weather.weather.adapter.DayForecastAdapter
import com.lab.esh1n.weather.weather.adapter.DayForecastSection
import com.lab.esh1n.weather.weather.adapter.DaytimeForecastModel
import com.lab.esh1n.weather.weather.model.ForecastDayModel
import com.lab.esh1n.weather.weather.viewmodel.DayForecastVM


class DayForecastFragment : BaseVMFragment<DayForecastVM>() {

    private lateinit var adapter: DayForecastAdapter
    private var recyclerView: RecyclerView? = null
    private var loadingIndicator: View? = null
    private var emptyView: View? = null

    override fun setupView(rootView: View, savedInstanceState: Bundle?) {
        super.setupView(rootView, savedInstanceState)
        recyclerView = rootView.findViewById(R.id.recycler_view)
        adapter = DayForecastAdapter()
        loadingIndicator = rootView.findViewById(R.id.loading_indicator)
        emptyView = rootView.findViewById(R.id.tv_no_items)
        recyclerView?.let {
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

        viewModel.sections.observe(viewLifecycleOwner, object : BaseObserver<List<Pair<DayForecastSection, List<DaytimeForecastModel>>>>() {
            override fun onData(data: List<Pair<DayForecastSection, List<DaytimeForecastModel>>>?) {
                data?.let {
                    adapter.updateForecastData(data)
                }
            }

            override fun onError(error: ErrorModel?) {
                SnackbarBuilder.buildErrorSnack(requireView(), error?.message ?: "").show()
            }

        })
        val dayModel = arguments?.getParcelable<ForecastDayModel>(ARG_PLACE_DAY_FORECAST)
        viewModel.loadDayForecastSections(PlaceDayArgs(dayDate = dayModel?.dayDate, placeId = dayModel?.placeId, timezone = dayModel?.timezone))


    }

    companion object {
        const val ARG_PLACE_DAY_FORECAST = "ARG_PLACE_DAY_FORECAST"
    }

    override val viewModelClass = DayForecastVM::class.java
    override val layoutResource = R.layout.fragment_recyclerview_with_progress
}