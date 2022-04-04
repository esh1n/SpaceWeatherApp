package com.lab.esh1n.weather.presentation.currentplace

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.esh1n.core_android.ui.addFragmentToStack
import com.esh1n.utils_android.ui.ScrollStateHolder
import com.esh1n.utils_android.ui.SnackbarBuilder
import com.esh1n.utils_android.ui.setVisibleOrGone
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.databinding.FragmentCurrentPlaceBinding
import com.lab.esh1n.weather.presentation.adapter.CurrentWeatherAdapter
import com.lab.esh1n.weather.presentation.fragment.ForecastFragment
import com.lab.esh1n.weather.presentation.model.WeatherModel
import com.lab.esh1n.weather.presentation.viewmodel.CurrentWeatherVM
import com.lab.esh1n.weather.utils.adapter
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class CurrentPlaceFragment : Fragment(R.layout.fragment_current_place) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: CurrentWeatherVM by viewModels() { viewModelFactory }

    //TODO migrate to view binding by Rozov
    private var binding: FragmentCurrentPlaceBinding? = null

    private fun onWeatherClicked(weatherModel: WeatherModel) =
        viewModel.openForecast(weatherModel.dayOfTheYear)

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
        observeWeather()
        viewModel.loadWeather()
    }

    private fun initView(view: View, savedInstanceState: Bundle?) {
        binding = DataBindingUtil.bind<FragmentCurrentPlaceBinding>(view)?.apply {
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.refresh()
            }
            val scrollStateHolder = ScrollStateHolder(savedInstanceState)
            listWeathers.layoutManager = LinearLayoutManager(requireActivity())
            listWeathers.addItemDecoration(
                DividerItemDecoration(
                    requireActivity(),
                    DividerItemDecoration.VERTICAL
                )
            )
            listWeathers.adapter = CurrentWeatherAdapter(::onWeatherClicked, scrollStateHolder)
        }
    }


    private fun observeWeather() {
        viewModel.openForecastEvent.observe(viewLifecycleOwner, ::openForecast)
        viewModel.currentWeatherUiState
            .observe(viewLifecycleOwner) { result ->
                result.onLoading { showLoading() }
                result.onComplete { hideLoading() }
                result.onFailure {
                    SnackbarBuilder.buildErrorSnack(requireView(), it.message).show()
                }
                result.onSuccess {
                    val (_, _, items) = it
                    binding?.run {
                        tvNoWeather.setVisibleOrGone(items.isEmpty())
                        listWeathers.adapter<CurrentWeatherAdapter>().swapWeathers(items)
                    }
                }

            }
        viewModel.refreshEffect.observe(viewLifecycleOwner) {
            it.onFailure {
                SnackbarBuilder.buildErrorSnack(requireView(), getString(R.string.error_unexpected))
                    .show()
            }
            it.onComplete { hideLoading() }
            it.onLoading { showLoading() }
            it.onSuccess {
                SnackbarBuilder.buildSnack(
                    requireView(),
                    getString(R.string.text_weather_updated_successfully),
                    duration = 500
                ).show()
            }
        }
    }

    private fun openForecast(openForecastArgs: OpenForecastArgs) {
        val (placeId, placeName, dayOfTheYear) = openForecastArgs
        parentFragment?.parentFragmentManager
            .addFragmentToStack(ForecastFragment.newInstance(placeId, placeName, dayOfTheYear))
    }

    private fun showLoading() {
        binding?.let {
            if (!it.swipeRefreshLayout.isRefreshing) {
                it.loadingIndicator.setVisibleOrGone(true)
            }
        }
    }

    private fun hideLoading() {
        binding?.let {
            if (it.swipeRefreshLayout.isRefreshing) {
                it.swipeRefreshLayout.isRefreshing = false
            } else {
                it.loadingIndicator.setVisibleOrGone(false)
            }
        }
    }

    companion object {
        fun newInstance() = CurrentPlaceFragment()
    }
}