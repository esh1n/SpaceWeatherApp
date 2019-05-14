package com.lab.esh1n.weather.base

import android.content.Context
import dagger.android.support.AndroidSupportInjection

/**
 * Created by esh1n on 3/16/18.
 */

abstract class BaseDIFragment : BaseFragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }
}
