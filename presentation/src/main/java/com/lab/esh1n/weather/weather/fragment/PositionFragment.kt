package com.lab.esh1n.weather.weather.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.lab.esh1n.weather.R
import kotlinx.android.synthetic.main.layout_position.*

const val ARG_POSITION = "position"

/**
 * Fragment to be displayed in [FragmentAdapter]
 *
 * Contains item position and displays it in TextView
 */
class PositionFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.layout_position, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val position = arguments?.getInt(ARG_POSITION) ?: 0
        showPosition(position)
    }

    private fun showPosition(position: Int) {
        val bgColorRes = if (position % 2 == 0) R.color.colorAccent else R.color.colorPrimaryDark
        val title = "Fragment ${position + 1}"
        name_tv.text = title
        name_tv.setBackgroundColor(ContextCompat.getColor(context ?: return, bgColorRes))
    }
}