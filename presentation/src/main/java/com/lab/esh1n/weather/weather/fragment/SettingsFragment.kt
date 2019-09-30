package com.lab.esh1n.weather.weather.fragment

import android.util.Log
import com.crashlytics.android.Crashlytics
import com.esh1n.core_android.ui.fragment.BaseVMFragment
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.weather.viewmodel.EmptyVM

class SettingsFragment : BaseVMFragment<EmptyVM>() {

    override val viewModelClass = EmptyVM::class.java

    override val layoutResource: Int = R.layout.fragment_settings

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onStart() {
        super.onStart()
        Crashlytics.logException(RuntimeException("test"))
        Crashlytics.log(Log.DEBUG, "SettingsFragment", "Settings opened")
    }
}