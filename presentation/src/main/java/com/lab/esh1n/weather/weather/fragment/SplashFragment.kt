package com.lab.esh1n.weather.weather.fragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import com.esh1n.core_android.ui.fragment.BaseVMFragment
import com.esh1n.core_android.ui.replaceFragment
import com.esh1n.utils_android.ui.SnackbarBuilder
import com.esh1n.utils_android.ui.setVisibleOrGone
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.utils.WORKER_ERROR_DESCRIPTION
import com.lab.esh1n.weather.weather.viewmodel.SplashVM

class SplashFragment : BaseVMFragment<SplashVM>() {

    override val viewModelClass = SplashVM::class.java

    override val layoutResource: Int = R.layout.fragment_splash

    private var fixPrepopulateButton: Button? = null
    override fun setupView(rootView: View, savedInstanceState: Bundle?) {
        super.setupView(rootView, savedInstanceState)
        fixPrepopulateButton = rootView.findViewById(R.id.button_fix_prepopulate)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.observeSync().observe(viewLifecycleOwner, Observer { infoList ->

            val allFinished = infoList.all { it.state.isFinished }
            if (allFinished) {
                val failure = infoList.find { it.state == WorkInfo.State.FAILED }
                if (failure != null) {
                    val failureDescription = failure.outputData.getString(WORKER_ERROR_DESCRIPTION)
                            ?: ""
                    SnackbarBuilder.buildErrorSnack(requireView(), failureDescription).show()
                    fixPrepopulateButton?.setVisibleOrGone(true)
                } else {
                    fragmentManager.replaceFragment(WeatherHostFragment.newInstance(), WeatherHostFragment::javaClass.name)
                }
            }
        })
        viewModel.startSync()
    }

    companion object {
        fun newInstance() = SplashFragment()
    }
}