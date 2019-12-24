package com.lab.esh1n.weather.weather.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.esh1n.core_android.error.ErrorModel
import com.esh1n.core_android.ui.addFragmentToStack
import com.esh1n.core_android.ui.fragment.BaseVMFragment
import com.esh1n.core_android.ui.setTitle
import com.esh1n.core_android.ui.viewmodel.BaseObserver
import com.esh1n.utils_android.ui.ScrollStateHolder
import com.esh1n.utils_android.ui.SnackbarBuilder
import com.esh1n.utils_android.ui.setVisibleOrGone
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.databinding.FragmentCurrentPlaceBinding
import com.lab.esh1n.weather.weather.adapter.CurrentWeatherAdapter
import com.lab.esh1n.weather.weather.model.CurrentWeatherModel
import com.lab.esh1n.weather.weather.model.WeatherModel
import com.lab.esh1n.weather.weather.viewmodel.CurrentWeatherVM


/**
 * Created by esh1n on 3/16/18.
 */

class CurrentPlaceFragment : BaseVMFragment<CurrentWeatherVM>() {


    override val viewModelClass = CurrentWeatherVM::class.java

    override val layoutResource = R.layout.fragment_current_place

    private var binding: FragmentCurrentPlaceBinding? = null

    private lateinit var adapter: CurrentWeatherAdapter

    private var title: String? = null

    private lateinit var scrollStateHolder: ScrollStateHolder

    private var placeId: Int? = null


    override fun setupView(rootView: View, savedInstanceState: Bundle?) {
        super.setupView(rootView, savedInstanceState)
        binding = DataBindingUtil.bind(rootView)
        binding?.let {
            it.swipeRefreshLayout.setOnRefreshListener {
                viewModel.refresh()
            }
            scrollStateHolder = ScrollStateHolder(savedInstanceState)
            adapter = CurrentWeatherAdapter(this::onWeatherClicked, scrollStateHolder)
            it.listWeathers.layoutManager = LinearLayoutManager(requireActivity())
            it.listWeathers.addItemDecoration(
                    DividerItemDecoration(
                            requireActivity(),
                            DividerItemDecoration.VERTICAL
                    )
            )

            it.listWeathers.setHasFixedSize(true)
            it.listWeathers.adapter = adapter
        }
    }


    private fun onWeatherClicked(weatherModel: WeatherModel) {
        placeId?.let {
            parentFragment?.fragmentManager.addFragmentToStack(ForecastFragment.newInstance(it))
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeWeather()
        viewModel.loadWeather()
        MobileAds.initialize(requireActivity().getApplication(), OnInitializationCompleteListener { status ->
            //initAdEvent.postValue(Resource.success(true))
        })
    }

    private fun getPlaceName(data: List<WeatherModel>?): String {
        val noData = data?.isNullOrEmpty() ?: false
        return if (noData)
            getString(R.string.menu_current_place) else
            (data!![0] as? CurrentWeatherModel)?.placeName ?: ""
    }

    private fun observeWeather() {
        viewModel.getWeatherLiveData().observe(this, object : BaseObserver<Pair<Int, List<WeatherModel>>>() {
            override fun onError(error: ErrorModel?) {
                SnackbarBuilder.buildErrorSnack(view!!, error?.message ?: "").show()
            }

            override fun onProgress(progress: Boolean) {
                super.onProgress(progress)
                showProgress(progress)
            }

            override fun onData(data: Pair<Int, List<WeatherModel>>?) {
                val weathers = data?.second
                val isEmpty = weathers?.isEmpty() ?: true
                binding?.tvNoWeather?.setVisibleOrGone(isEmpty)
                val placeName = getPlaceName(weathers)
                setTitle(placeName)
                title = placeName
                adapter.swapWeathers(weathers.orEmpty())
                placeId = data?.first
            }
        })
        viewModel.refreshOperation.observe(this, object : BaseObserver<Unit>() {
            override fun onData(data: Unit?) {

                SnackbarBuilder.buildSnack(view!!, getString(R.string.text_weather_updated_successfully), duration = 500).show()

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

    fun getTitle(): CharSequence {
        return title ?: ""
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    companion object {
        const val TAG = "CurrentPlaceFragment"
        fun newInstance() = CurrentPlaceFragment()
    }
}