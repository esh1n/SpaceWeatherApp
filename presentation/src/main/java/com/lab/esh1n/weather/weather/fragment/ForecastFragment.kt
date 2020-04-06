package com.lab.esh1n.weather.weather.fragment

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.esh1n.core_android.error.ErrorModel
import com.esh1n.core_android.ui.fragment.BaseVMFragment
import com.esh1n.core_android.ui.setTitle
import com.esh1n.core_android.ui.viewmodel.BaseObserver
import com.esh1n.utils_android.ui.SnackbarBuilder
import com.esh1n.utils_android.ui.setVisibleOrGone
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.weather.adapter.DayForecastFragmentAdapter
import com.lab.esh1n.weather.weather.model.ForecastDayModel
import com.lab.esh1n.weather.weather.viewmodel.ForecastWeekVM

class ForecastFragment : BaseVMFragment<ForecastWeekVM>() {
    //TODO use ViewBinding here
    override val viewModelClass = ForecastWeekVM::class.java

    override val layoutResource: Int = R.layout.fragment_forecast

    private var viewPager: ViewPager2? = null

    private var tabs: TabLayout? = null

    private var loadingIndicator: View? = null


    private lateinit var adapterDayForecast: DayForecastFragmentAdapter

    companion object {
        private const val PLACE_ID = "PLACE_ID"
        private const val PLACE_NAME = "PLACE_NAME"
        private const val SELECTED_DAY = "SELECTED_DAY"
        fun newInstance(placeId: Int, placeName: String, selectedDay: Int = 0) = ForecastFragment().apply {
            arguments = Bundle().apply {
                putInt(PLACE_ID, placeId)
                putString(PLACE_NAME, placeName)
                putInt(SELECTED_DAY, selectedDay)
            }
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

    override fun setupView(rootView: View, savedInstanceState: Bundle?) {
        super.setupView(rootView, savedInstanceState)
        viewPager = rootView.findViewById(R.id.viewpager)
        tabs = rootView.findViewById(R.id.tabs)
        loadingIndicator = rootView.findViewById(R.id.loading_indicator)

        adapterDayForecast = DayForecastFragmentAdapter(requireActivity())

        viewPager?.let {
            it.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    Log.d("OnPageChangeCallback", "Page selected: $position")
                }
            })
            it.adapter = adapterDayForecast

            if (tabs != null) {
                Log.d("TABS", "init tabs")
                TabLayoutMediator(tabs!!, it,
                        TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                            val days = adapterDayForecast.publicDays
                            if (position >= 0 && position < days.size) {
                                tab.text = days[position].dayDescription
                            }

                        }).attach()
            }
        }
    }

    private fun getPlaceId(): Int? {
        return arguments?.getInt(PLACE_ID)
    }

    private fun getPlaceName(): String? {
        return arguments?.getString(PLACE_NAME)
    }

    private fun getSelectedDay(): Int {
        return arguments?.getInt(SELECTED_DAY, 0) ?: 0
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.availableDays.observe(viewLifecycleOwner, object : BaseObserver<Pair<Int, List<ForecastDayModel>>>() {
            override fun onData(data: Pair<Int, List<ForecastDayModel>>?) {
                data?.let {
                    populateByDays(data.first, data.second)
                }
            }

            override fun onProgress(progress: Boolean) {
                super.onProgress(progress)
                loadingIndicator?.setVisibleOrGone(progress)
            }

            override fun onError(error: ErrorModel?) {
                SnackbarBuilder.buildErrorSnack(requireView(), error?.message ?: "").show()
            }

        })
        viewModel.fetchForecastEvent.observe(viewLifecycleOwner, object : BaseObserver<Unit>() {
            override fun onData(data: Unit?) {

            }

            override fun onError(error: ErrorModel?) {
                FirebaseCrashlytics.getInstance().recordException(RuntimeException(error?.message
                        ?: ""))
                SnackbarBuilder.buildErrorSnack(requireView(), error?.message ?: "").show()
            }

        })
        getPlaceId()?.let { placeId ->
            viewModel.loadAvailableDays(placeId, getSelectedDay())
            viewModel.fetchForecastIfNeeded(placeId)
        }
        setTitle(getPlaceName() ?: "")

    }

    fun populateByDays(selectedDayIndex: Int, items: List<ForecastDayModel>) {
        adapterDayForecast.publicDays = items
        if (selectedDayIndex >= 0) {
            viewPager?.currentItem = selectedDayIndex
        }

    }
}