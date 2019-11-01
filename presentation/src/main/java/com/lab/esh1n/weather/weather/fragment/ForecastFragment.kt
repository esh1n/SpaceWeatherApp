package com.lab.esh1n.weather.weather.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.esh1n.core_android.ui.fragment.BaseVMFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.weather.adapter.FragmentAdapter
import com.lab.esh1n.weather.weather.viewmodel.EmptyVM

class ForecastFragment : BaseVMFragment<EmptyVM>() {

    override val viewModelClass = EmptyVM::class.java

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
                .apply { items = listOf(0, 1, 2) }


        viewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                Log.d("OnPageChangeCallback", "Page selected: $position")
            }
        })

        viewPager?.adapter = adapter
        if (tabs != null && viewPager != null) {
            Log.d("TABS", "init tabs")
            TabLayoutMediator(tabs!!, viewPager!!,
                    TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                        when (position) {
                            0 -> {
                                tab.text = "TAB ONE"
                            }
                            1 -> {
                                tab.text = "TAB TWO"
                            }
                            2 -> {
                                tab.text = "TAB 3"
                            }
                        }
                    }).attach()
        }


    }
}