package com.lab.esh1n.weather.weather.fragment

import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.base.BaseVMFragment
import com.lab.esh1n.weather.weather.viewmodel.AllPlacesViewModel

class AllPlacesFragment : BaseVMFragment<AllPlacesViewModel>() {

    override val viewModelClass = AllPlacesViewModel::class.java

    override val layoutResource = R.layout.fragment_all_places

    companion object {
        fun newInstance() = AllPlacesFragment()
    }
}