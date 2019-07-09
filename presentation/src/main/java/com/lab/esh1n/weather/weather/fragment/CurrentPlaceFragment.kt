package com.lab.esh1n.weather.weather.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.base.BaseObserver
import com.lab.esh1n.weather.base.BaseVMFragment
import com.lab.esh1n.weather.databinding.FragmentWeatherBinding
import com.lab.esh1n.weather.domain.base.ErrorModel
import com.lab.esh1n.weather.utils.SnackbarBuilder
import com.lab.esh1n.weather.utils.loadCircleImage
import com.lab.esh1n.weather.utils.setVisibleOrGone
import com.lab.esh1n.weather.weather.WeatherModel
import com.lab.esh1n.weather.weather.viewmodel.CurrentWeatherVM

/**
 * Created by esh1n on 3/16/18.
 */

class CurrentPlaceFragment : BaseVMFragment<CurrentWeatherVM>() {


    override val viewModelClass = CurrentWeatherVM::class.java

    override val layoutResource = R.layout.fragment_weather

    private var binding: FragmentWeatherBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.startPeriodicSync()
    }

    override fun setupView(rootView: View) {
        super.setupView(rootView)
        binding = DataBindingUtil.bind(rootView)
        binding?.let {
            it.swipeRefreshLayout.setOnRefreshListener {
                viewModel.refresh()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // SyncWeatherService.start(requireContext())
        observeWeather()
    }

    private fun observeWeather() {
        viewModel.weatherLiveData.observe(this, object : BaseObserver<WeatherModel>() {
            override fun onData(data: WeatherModel?) {
                if (data != null) {
                    showContent()
                    bindEventToView(data)
                } else {
                    showEmptyView()
                }
            }

            override fun onError(error: ErrorModel?) {
                SnackbarBuilder.buildErrorSnack(view!!, error?.message ?: "").show()
            }

            override fun onProgress(progress: Boolean) {
                super.onProgress(progress)
                showProgress(progress)
            }

        })
        viewModel.refreshOperation.observe(this, object : BaseObserver<Unit>() {
            override fun onData(data: Unit?) {
                data?.let {
                    SnackbarBuilder.buildSnack(view!!, getString(R.string.text_weather_updated_successfully),duration = 500).show()
                }
            }

            override fun onError(error: ErrorModel?) {
                SnackbarBuilder.buildErrorSnack(view!!, getString(R.string.error_unexpected)).show()
            }

            override fun onProgress(progress: Boolean) {
                super.onProgress(progress)
                showProgress(progress)
            }

        })

    }

    private fun showProgress(progress: Boolean) {
        if (progress) {
            showLoading()
        } else {
            hideLoading()
        }
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


    private fun showEmptyView() {
        binding?.let {
            it.viewEmpty.setVisibleOrGone(true)
            it.viewContent.setVisibleOrGone(false)
        }
    }

    private fun showContent() {
        binding?.let {
            it.viewEmpty.setVisibleOrGone(false)
            it.viewContent.setVisibleOrGone(true)
        }
    }

    private fun bindEventToView(weatherModel: WeatherModel) {
        binding?.let {
            it.weather = weatherModel
            it.ivIcon.loadCircleImage(weatherModel.iconUrl)
            it.executePendingBindings()
        }
    }

    companion object {
        fun newInstance() = CurrentPlaceFragment()
    }
}