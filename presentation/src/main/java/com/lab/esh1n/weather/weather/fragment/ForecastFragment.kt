package com.lab.esh1n.weather.weather.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.esh1n.core_android.error.ErrorModel
import com.esh1n.core_android.ui.fragment.BaseVMFragment
import com.esh1n.core_android.ui.viewmodel.BaseObserver
import com.esh1n.utils_android.ui.SnackbarBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.weather.adapter.FragmentAdapter
import com.lab.esh1n.weather.weather.model.ForecastDayModel
import com.lab.esh1n.weather.weather.viewmodel.ForecastWeekVM

class ForecastFragment : BaseVMFragment<ForecastWeekVM>() {

    override val viewModelClass = ForecastWeekVM::class.java

    override val layoutResource: Int = R.layout.fragment_forecast

    private var viewPager: ViewPager2? = null

    private var tabs: TabLayout? = null

    private lateinit var adapter: FragmentAdapter

    companion object {
        private const val PLACE_ID = "PLACE_ID"
        fun newInstance(placeId: Int) = ForecastFragment().apply {
            arguments = Bundle().apply { putInt(PLACE_ID, placeId) }
        }
    }

    override fun setupView(rootView: View, savedInstanceState: Bundle?) {
        super.setupView(rootView, savedInstanceState)
        viewPager = rootView.findViewById(R.id.viewpager)
        tabs = rootView.findViewById(R.id.tabs)

        adapter = FragmentAdapter(requireActivity())

        viewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                Log.d("OnPageChangeCallback", "Page selected: $position")
            }
        })
        viewPager?.adapter = adapter
    }

    private fun getPlaceId(): Int? {
        return arguments?.getInt(PLACE_ID)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.availableDays.observe(viewLifecycleOwner, object : BaseObserver<List<ForecastDayModel>>() {
            override fun onData(data: List<ForecastDayModel>?) {
                data?.let {
                    populateByDays(data)
                }
            }

            override fun onError(error: ErrorModel?) {
                SnackbarBuilder.buildErrorSnack(requireView(), error?.message ?: "").show()
            }

        })
        getPlaceId()?.let { placeId ->
            viewModel.loadAvailableDays(placeId)
        }

    }

    fun populateByDays(items: List<ForecastDayModel>) {
        adapter.items = items
        if (tabs != null && viewPager != null) {
            Log.d("TABS", "init tabs")
            TabLayoutMediator(tabs!!, viewPager!!,
                    TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                        tab.text = items[position].dayDescription
                    }).attach()
        }
    }
}