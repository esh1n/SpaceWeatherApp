package com.lab.esh1n.weather.weather.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lab.esh1n.weather.R
import kotlinx.android.synthetic.main.layout_position.*

const val ARG_TITLE = "ARG_TITLE"

/**
 * Fragment to be displayed in [FragmentAdapter]
 *
 * Contains item position and displays it in TextView
 */
class DayForecastFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.layout_position, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val position = arguments?.getString(ARG_TITLE) ?: ""
        showPosition(position)
    }

    private fun showPosition(description: String) {
        name_tv.text = description
    }
}