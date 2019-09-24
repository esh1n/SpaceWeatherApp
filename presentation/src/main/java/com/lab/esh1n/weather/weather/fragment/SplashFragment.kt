package com.lab.esh1n.weather.weather.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import com.esh1n.core_android.ui.fragment.BaseVMFragment
import com.esh1n.core_android.ui.replaceFragment
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.weather.viewmodel.SplashViewModel

class SplashFragment : BaseVMFragment<SplashViewModel>() {

    override val viewModelClass = SplashViewModel::class.java

    override val layoutResource: Int = R.layout.fragment_settings

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.observeSync().observe(viewLifecycleOwner, Observer { infoList ->
            val allSuccess = infoList.all { it.state == WorkInfo.State.SUCCEEDED }
            if (allSuccess) {
                requireActivity().replaceFragment(new)
            }
        })
        viewModel.startSync()
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}