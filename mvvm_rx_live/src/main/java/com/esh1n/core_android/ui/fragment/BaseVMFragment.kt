package com.esh1n.core_android.ui.fragment


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import javax.inject.Inject

abstract class BaseVMFragment<VM : ViewModel> : BaseDIFragment() {

    abstract val viewModelClass: Class<VM>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected val viewModel: VM
            by lazy {
                ViewModelProviders.of(this, viewModelFactory)
                        .get(viewModelClass)
            }

}