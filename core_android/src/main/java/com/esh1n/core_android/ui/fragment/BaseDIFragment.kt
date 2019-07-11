package com.esh1n.core_android.ui.fragment

import android.content.Context
import dagger.android.support.AndroidSupportInjection

abstract class BaseDIFragment: BaseFragment() {
    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }
}