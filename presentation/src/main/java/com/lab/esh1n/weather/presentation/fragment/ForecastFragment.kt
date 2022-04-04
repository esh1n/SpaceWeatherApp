package com.lab.esh1n.weather.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.esh1n.core_android.error.ErrorModel
import com.esh1n.core_android.ui.fragment.BaseVMFragment
import com.esh1n.core_android.ui.setTitle
import com.esh1n.core_android.ui.viewmodel.BaseObserver
import com.esh1n.utils_android.ui.SnackbarBuilder
import com.esh1n.utils_android.ui.setVisibleOrGone
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.databinding.FragmentForecastBinding
import com.lab.esh1n.weather.presentation.adapter.DayForecastFragmentAdapter
import com.lab.esh1n.weather.presentation.model.ForecastDayModel
import com.lab.esh1n.weather.presentation.viewmodel.ForecastWeekVM
import com.lab.esh1n.weather.utils.autoDestroyViewDelegate
import kotlinx.coroutines.launch

class ForecastFragment : BaseVMFragment<ForecastWeekVM>() {

    override val viewModelClass = ForecastWeekVM::class.java

    override val layoutResource: Int = R.layout.fragment_forecast

    private var binding: FragmentForecastBinding by autoDestroyViewDelegate()

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
        binding = FragmentForecastBinding.bind(rootView)

        adapterDayForecast = DayForecastFragmentAdapter(requireActivity())

        binding.viewpager.let {
            it.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    Log.d("OnPageChangeCallback", "Page selected: $position")
                }
            })
            it.adapter = adapterDayForecast

            Log.d("TABS", "init tabs")
            TabLayoutMediator(binding.tabs, it) { tab, position ->
                val days = adapterDayForecast.publicDays
                if (position >= 0 && position < days.size) {
                    tab.text = days[position].dayDescription
                }

            }.attach()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.availableDays.observe(
            viewLifecycleOwner,
            object : BaseObserver<Pair<Int, List<ForecastDayModel>>>() {
                override fun onData(data: Pair<Int, List<ForecastDayModel>>?) {
                    data?.let {
                        populateByDays(data.first, data.second)
                    }
                }

                override fun onProgress(progress: Boolean) {
                    super.onProgress(progress)
                    binding.loadingIndicator.setVisibleOrGone(progress)
            }

            override fun onError(error: ErrorModel?) {
                SnackbarBuilder.buildErrorSnack(requireView(), error?.message ?: "").show()
            }

        })
        viewModel.fetchForecastEvent.observe(viewLifecycleOwner, object : BaseObserver<Unit>() {
            override fun onData(data: Unit?) {

            }

            override fun onError(error: ErrorModel?) {
                FirebaseCrashlytics.getInstance().recordException(
                    RuntimeException(
                        error?.message
                            ?: ""
                    )
                )
                SnackbarBuilder.buildErrorSnack(requireView(), error?.message ?: "").show()
            }

        })
        getPlaceId()?.let { placeId ->
            viewModel.loadAvailableDays(placeId, getSelectedDay())
            viewModel.fetchForecastIfNeeded(placeId)
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.getFavouriteStateFlow(placeId)
                    .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                    .collect {
                        binding.switchFavourites.isChecked = it
                    }
            }
            binding.switchFavourites.setOnCheckedChangeListener { _, checked ->
                viewModel.changeFavouriteState(placeId, checked)
            }
        }

        getPlaceName()?.let(::setTitle)
    }

    fun populateByDays(selectedDayIndex: Int, items: List<ForecastDayModel>) {
        adapterDayForecast.publicDays = items
        if (selectedDayIndex >= 0) {
            binding.viewpager.currentItem = selectedDayIndex
        }
    }
}