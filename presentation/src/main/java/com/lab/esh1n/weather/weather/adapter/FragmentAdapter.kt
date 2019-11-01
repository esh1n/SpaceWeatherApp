package com.lab.esh1n.weather.weather.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView.NO_ID
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lab.esh1n.weather.weather.fragment.ARG_POSITION
import com.lab.esh1n.weather.weather.fragment.PositionFragment


/**
 * Adapter for fragments
 */
class FragmentAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    var items: List<Int>
        set(value) {
            mutableItems = value.toMutableList()
            notifyDataSetChanged()
        }
        get() = mutableItems

    private var mutableItems = mutableListOf<Int>()

    override fun createFragment(position: Int): Fragment {
        return PositionFragment::class.java.newInstance()
                .apply {
                    arguments = Bundle().apply {
                        putInt(ARG_POSITION, items[position])
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
        return items[position].toLong()
    }

    /**
     * Method override to support notify* methods
     *
     * @see FragmentStateAdapter.containsItem
     */
    override fun containsItem(itemId: Long): Boolean {
        return items.contains(itemId.toInt())
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
    fun addAfter(position: Int) {
        val size = mutableItems.size
        if (size == 0 || position == size) mutableItems.add(size) else mutableItems.add(position + 1, size)
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