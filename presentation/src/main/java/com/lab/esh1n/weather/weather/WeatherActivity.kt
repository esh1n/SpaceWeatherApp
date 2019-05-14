package com.lab.esh1n.weather.weather

import android.os.Bundle
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.base.BaseActivity
import com.lab.esh1n.weather.utils.addSingleFragmentToContainer
import com.lab.esh1n.weather.weather.fragment.WeatherFragment

/**
 * Created by esh1n on 3/16/18.
 */

class WeatherActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addSingleFragmentToContainer(WeatherFragment.newInstance())
    }
}