package com.lab.esh1n.weather.weather.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.base.BaseObserver
import com.lab.esh1n.weather.base.BaseVMFragment
import com.lab.esh1n.weather.domain.base.ErrorModel
import com.lab.esh1n.weather.utils.SnackbarBuilder
import com.lab.esh1n.weather.utils.openUrl
import com.lab.esh1n.weather.weather.WeatherModel
import com.lab.esh1n.weather.weather.viewmodel.EventDetailViewModel
import com.lab.esh1n.weather.weather.viewmodel.SharedEventViewModel

class EventDetailFragment : BaseVMFragment<EventDetailViewModel>() {
    override val viewModelClass = EventDetailViewModel::class.java
    override val layoutResource = R.layout.fragment_weather
    private var binding: FragmentEventDetailsBinding? = null
    private lateinit var sharedEventViewModel: SharedEventViewModel

    override fun setupView(rootView: View) {
        super.setupView(rootView)
        binding = DataBindingUtil.bind(rootView)
        binding?.let {
            it.tvLink.setOnClickListener { _ ->
                requireActivity().openUrl(it.event?.repositoryLink)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.event.observe(this, object : BaseObserver<WeatherModel>() {
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

        })
        sharedEventViewModel = ViewModelProviders.of(requireActivity()).get(SharedEventViewModel::class.java)
        sharedEventViewModel.eventId.observe(this, Observer { eventId ->
            if (eventId != null && eventId > 0) {
                viewModel.loadEvent(eventId)
            }
        })
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
            it.event = weatherModel
            it.ivAvatar.loadCircleImage(weatherModel.actorAvatar)
            it.executePendingBindings()
        }
    }

    companion object {
        fun newInstance() = EventDetailFragment()
    }

}