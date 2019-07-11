package com.esh1n.core_android.ui.fragment

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.esh1n.core_android.ui.viewmodel.BaseViewModel

abstract class BaseVMFragment<VM : BaseViewModel> : BaseDIFragment() {

    protected lateinit var viewModel: VM

    abstract val viewModelClass: Class<VM>

    abstract val factory: ViewModelProvider.Factory

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory).get(viewModelClass)
        onViewModelInitialized()
    }

    open fun onViewModelInitialized() {}

}
