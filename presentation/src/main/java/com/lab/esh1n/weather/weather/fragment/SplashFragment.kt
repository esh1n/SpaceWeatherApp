package com.lab.esh1n.weather.weather.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatTextView
import com.esh1n.core_android.error.ErrorModel
import com.esh1n.core_android.ui.fragment.BaseVMFragment
import com.esh1n.core_android.ui.replaceFragment
import com.esh1n.core_android.ui.viewmodel.BaseObserver
import com.esh1n.utils_android.ui.SnackbarBuilder
import com.esh1n.utils_android.ui.setVisibleOrGone
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.domain.weather.ProgressModel
import com.lab.esh1n.weather.weather.viewmodel.SplashVM

class SplashFragment : BaseVMFragment<SplashVM>() {

    override val viewModelClass = SplashVM::class.java

    override val layoutResource: Int = R.layout.fragment_splash

    private var fixPrepopulateButton: Button? = null
    private var progressBar: ProgressBar? = null
    private var progressDescription: AppCompatTextView? = null
    override fun setupView(rootView: View, savedInstanceState: Bundle?) {
        super.setupView(rootView, savedInstanceState)
        fixPrepopulateButton = rootView.findViewById(R.id.button_fix_prepopulate)
        progressBar = rootView.findViewById(R.id.pb_prepopulate)
        progressDescription = rootView.findViewById(R.id.tv_description)

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
                fixPrepopulateButton?.setVisibleOrGone(true)
            }

        })
        viewModel.startPrepopulate()
    }

    private fun showProgress(progressModel: ProgressModel<Unit>) {
        Handler(Looper.getMainLooper()).post {
            progressBar?.progress = progressModel.progress
            if (progressModel.description != progressDescription?.text.toString()) {
                progressDescription?.text = progressModel.description
            }

        }
    }

    private fun showMainScreen() {
        fragmentManager.replaceFragment(WeatherHostFragment.newInstance(), WeatherHostFragment::javaClass.name)
    }

    companion object {
        fun newInstance() = SplashFragment()
    }
}