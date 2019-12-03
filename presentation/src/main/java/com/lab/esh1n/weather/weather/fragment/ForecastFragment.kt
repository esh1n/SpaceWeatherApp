package com.lab.esh1n.weather.weather.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.esh1n.core_android.error.ErrorModel
import com.esh1n.core_android.ui.fragment.BaseVMFragment
import com.esh1n.core_android.ui.viewmodel.BaseObserver
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
        fun newInstance() = ForecastFragment()
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.availableDays.observe(viewLifecycleOwner, object : BaseObserver<List<ForecastDayModel>>() {
            override fun onData(data: List<ForecastDayModel>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onError(error: ErrorModel?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
        viewModel.loadAvailableDays()
    }

    fun populateByDays(items: List<Int>) {
        adapter.items = items
        if (tabs != null && viewPager != null) {
            Log.d("TABS", "init tabs")
            TabLayoutMediator(tabs!!, viewPager!!,
                    TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                        tab.text = "TAB ${items[position]}"
                    }).attach()
        }
    }
}