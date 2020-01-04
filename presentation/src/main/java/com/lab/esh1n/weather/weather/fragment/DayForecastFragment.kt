package com.lab.esh1n.weather.weather.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.weather.model.ForecastDayModel
import kotlinx.android.synthetic.main.layout_position.*


/**
 * Fragment to be displayed in [FragmentAdapter]
 *
 * Contains item position and displays it in TextView
 */
class DayForecastFragment : Fragment(R.layout.layout_position) {


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dayModel = arguments?.getParcelable<ForecastDayModel>(ARG_PLACE_DAY_FORECAST)
        showTitle(dayModel?.dayDescription ?: "")
    }

    private fun showTitle(description: String) {
        name_tv.text = description
    }

    companion object {
        const val ARG_PLACE_DAY_FORECAST = "ARG_PLACE_DAY_FORECAST"
    }
}