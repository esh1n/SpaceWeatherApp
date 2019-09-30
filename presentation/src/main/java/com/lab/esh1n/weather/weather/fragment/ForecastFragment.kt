package com.lab.esh1n.weather.weather.fragment

import com.esh1n.core_android.ui.fragment.BaseVMFragment
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.weather.viewmodel.EmptyVM

class ForecastFragment : BaseVMFragment<EmptyVM>() {

    override val viewModelClass = EmptyVM::class.java

    override val layoutResource: Int = R.layout.fragment_forecast

    companion object {
        fun newInstance() = ForecastFragment()
    }
}