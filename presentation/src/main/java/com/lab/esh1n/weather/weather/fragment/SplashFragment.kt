package com.lab.esh1n.weather.weather.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.esh1n.core_android.error.ErrorModel
import com.esh1n.core_android.ui.fragment.BaseVMFragment
import com.esh1n.core_android.ui.replaceFragment
import com.esh1n.core_android.ui.viewmodel.BaseObserver
import com.esh1n.utils_android.ui.SnackbarBuilder
import com.esh1n.utils_android.ui.setVisibleOrGone
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.databinding.FragmentSplashBinding
import com.lab.esh1n.weather.domain.weather.ProgressModel
import com.lab.esh1n.weather.utils.viewLifecycle
import com.lab.esh1n.weather.weather.viewmodel.SplashVM

class SplashFragment : BaseVMFragment<SplashVM>() {

    override val viewModelClass = SplashVM::class.java

    override val layoutResource: Int = R.layout.fragment_splash

    private var binding: FragmentSplashBinding by viewLifecycle()

    override fun setupView(rootView: View, savedInstanceState: Bundle?) {
        super.setupView(rootView, savedInstanceState)
        binding = FragmentSplashBinding.bind(rootView)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.prepopulateEvent.observe(viewLifecycleOwner, object : BaseObserver<ProgressModel<Unit>>() {
            override fun onData(data: ProgressModel<Unit>?) {
                data?.let {
                    if (data.isDone) {
                        showMainScreen()
                    } else {
                        showProgress(data)
                    }
                }
            }

            override fun onError(error: ErrorModel?) {
                SnackbarBuilder.buildErrorSnack(requireView(), error?.message ?: "").show()
                binding.buttonFixPrepopulate.setVisibleOrGone(true)
            }

        })
        viewModel.startPrepopulate()
    }

    private fun showProgress(progressModel: ProgressModel<Unit>) {
        Handler(Looper.getMainLooper()).post {
            binding.pbPrepopulate.progress = progressModel.progress
            if (progressModel.description != binding.tvDescription.text.toString()) {
                binding.tvDescription.text = progressModel.description
            }

        }
    }

    private fun showMainScreen() {
        parentFragmentManager.replaceFragment(WeatherHostFragment.newInstance(), WeatherHostFragment::javaClass.name)
    }

    companion object {
        fun newInstance() = SplashFragment()
    }
}