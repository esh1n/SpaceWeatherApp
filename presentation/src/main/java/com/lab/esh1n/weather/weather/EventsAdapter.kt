package com.lab.esh1n.weather.weather

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.databinding.ItemEventBinding
import com.lab.esh1n.weather.utils.inflate

/**
 * Created by esh1n on 3/18/18.
 */

class EventsAdapter(private val clickHandler: (WeatherModel) -> Unit) :
        RecyclerView.Adapter<EventsAdapter.ViewHolder>() {

    private var weathers: List<WeatherModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.context.inflate(R.layout.item_event, parent))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = weathers[position]
        holder.populate(person)
    }

    override fun getItemCount() = weathers.size

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private val binding: ItemEventBinding? = DataBindingUtil.bind(itemView)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val adapterPosition = adapterPosition
            val userModel = weathers[adapterPosition]
            clickHandler.invoke(userModel)
        }

        fun populate(weatherModel: WeatherModel?) {
            if (weatherModel != null && binding != null) {
                binding.event = weatherModel
                binding.executePendingBindings()
                binding.ivAvatar.loadCircleImage(weatherModel.actorAvatar)
            }
        }
    }

    fun swapEvents(newUsers: List<WeatherModel>) {
        if (!this.weathers.isEmpty()) {
            val result = DiffUtil.calculateDiff(DiffUtilCallback(this.weathers, newUsers))
            this.weathers = newUsers
            result.dispatchUpdatesTo(this)
        } else {
            this.weathers = newUsers
            notifyDataSetChanged()
        }
    }

    private class DiffUtilCallback(private val oldList: List<WeatherModel>,
                                   private val newList: List<WeatherModel>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}