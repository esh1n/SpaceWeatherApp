package com.lab.esh1n.weather.weather.fragment

import com.esh1n.core_android.ui.fragment.BaseVMFragment
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.weather.viewmodel.SettingsViewModel

class SettingsFragment : BaseVMFragment<SettingsViewModel>() {

    override val viewModelClass = SettingsViewModel::class.java

    override val layoutResource: Int = R.layout.fragment_settings

    companion object {
        fun newInstance() = SettingsFragment()
    }
}