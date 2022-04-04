package com.lab.esh1n.weather.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.NO_ID
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lab.esh1n.weather.presentation.fragment.DayForecastFragment
import com.lab.esh1n.weather.presentation.model.ForecastDayModel


/**
 * Adapter for fragments
 */
class DayForecastFragmentAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    var publicDays: List<ForecastDayModel>
        set(value) {
            swapDays(value)
        }
        get() = innerDays

    private var innerDays = mutableListOf<ForecastDayModel>()

    private fun swapDays(newDays: List<ForecastDayModel>) {
        if (innerDays.isNullOrEmpty()) {
            this.innerDays = newDays.toMutableList()
            notifyDataSetChanged()
        } else {
            val result = DiffUtil.calculateDiff(DiffCallback(innerDays, newDays))
            this.innerDays = newDays.toMutableList()
            result.dispatchUpdatesTo(this)
        }
    }

    private inner class DiffCallback(private val oldBrands: List<ForecastDayModel>, private val newBrands: List<ForecastDayModel>) :
            DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldBrands.size
        }

        override fun getNewListSize(): Int {
            return newBrands.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val humanDate = oldBrands[oldItemPosition].dayDescription
            val humanDate1 = newBrands[newItemPosition].dayDescription
            return humanDate == humanDate1
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldBrand = oldBrands[oldItemPosition]
            val newBrand = newBrands[newItemPosition]
            return oldBrand == newBrand
        }
    }

    override fun createFragment(position: Int): Fragment {
        return DayForecastFragment.newInstance(publicDays[position])
    }

    /**
     * Method override to support notify* methods
     *
     * @see FragmentStateAdapter.getItemId
     */
    override fun getItemId(position: Int): Long {
        if (position < 0) return NO_ID
        return publicDays[position].dayDate.time
    }

    /**
     * Method override to support notify* methods
     *
     * @see FragmentStateAdapter.containsItem
     */
    override fun containsItem(itemId: Long): Boolean {
        return publicDays.find { it.dayDate.time == itemId } != null
    }

    /**
     * @see FragmentStateAdapter.getItemCount
     */
    override fun getItemCount(): Int = publicDays.size

    /**
     * Add an item which equals to list size after certain position in list
     *
     * @param position position of item to add
     */

    fun addAfter(dayModel: ForecastDayModel, position: Int) {
        val size = innerDays.size
        if (size == 0 || position == size) innerDays.add(dayModel) else innerDays.add(position + 1, dayModel)
        notifyItemInserted(position)
    }

    /**
     * Delete an item at certain position in list
     *
     * @param position position of item to remove
     */
    fun deleteAt(position: Int) {
        innerDays.removeAt(position)
        notifyItemRemoved(position)
    }
}