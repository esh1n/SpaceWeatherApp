package com.lab.esh1n.weather.weather

import android.os.Bundle
import com.esh1n.core_android.ui.activity.BaseToolbarActivity
import com.esh1n.core_android.ui.addSingleFragmentToContainer
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.weather.fragment.WeatherHostFragment

/**
 * Created by esh1n on 3/16/18.
 */

class WeatherActivity : BaseToolbarActivity() {
    override val toolbarId = R.id.toolbar
    override val contentViewResourceId = R.layout.activity_main
    override val isDisplayHomeAsUpEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addSingleFragmentToContainer(WeatherHostFragment.newInstance())
        initFragmentTransactionsListener()
    }

    private fun initFragmentTransactionsListener() {
        supportFragmentManager.addOnBackStackChangedListener { this.processFragmentsSwitching() }
    }

    private fun processFragmentsSwitching() {

        supportFragmentManager.let {
            val isInRootFragment = it.backStackEntryCount == 0
            this.showHomeAsUpButton(!isInRootFragment)
        }

    }
}