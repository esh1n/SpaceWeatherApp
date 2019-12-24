package com.lab.esh1n.weather.weather.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView.NO_ID
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lab.esh1n.weather.weather.fragment.ARG_TITLE
import com.lab.esh1n.weather.weather.fragment.DayForecastFragment
import com.lab.esh1n.weather.weather.model.ForecastDayModel


/**
 * Adapter for fragments
 */
class FragmentAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    var items: List<ForecastDayModel>
        set(value) {
            mutableItems = value.toMutableList()
            notifyDataSetChanged()
        }
        get() = mutableItems

    private var mutableItems = mutableListOf<ForecastDayModel>()

    override fun createFragment(position: Int): Fragment {
        return DayForecastFragment::class.java.newInstance()
                .apply {
                    arguments = Bundle().apply {
                        putString(ARG_TITLE, items[position].dayDescription)
                    }
                }
    }

    /**
     * Method override to support notify* methods
     *
     * @see FragmentStateAdapter.getItemId
     */
    override fun getItemId(position: Int): Long {
        if (position < 0) return NO_ID
        return items[position].dayDate.time
    }

    /**
     * Method override to support notify* methods
     *
     * @see FragmentStateAdapter.containsItem
     */
    override fun containsItem(itemId: Long): Boolean {
        return items.find { it.dayDate.time == itemId } != null
    }

    /**
     * @see FragmentStateAdapter.getItemCount
     */
    override fun getItemCount(): Int = items.size

    /**
     * Add an item which equals to list size after certain position in list
     *
     * @param position position of item to add
     */

    fun addAfter(dayModel: ForecastDayModel, position: Int) {
        val size = mutableItems.size
        if (size == 0 || position == size) mutableItems.add(dayModel) else mutableItems.add(position + 1, dayModel)
        notifyItemInserted(position)
    }

    /**
     * Delete an item at certain position in list
     *
     * @param position position of item to remove
     */
    fun deleteAt(position: Int) {
        mutableItems.removeAt(position)
        notifyItemRemoved(position)
    }
}