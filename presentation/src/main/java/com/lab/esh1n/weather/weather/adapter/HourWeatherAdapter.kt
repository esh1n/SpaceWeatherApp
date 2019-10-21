package com.lab.esh1n.weather.weather.adapter

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.esh1n.utils_android.ui.inflate
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.databinding.ItemHourWeatherBinding
import com.lab.esh1n.weather.databinding.ItemHourWeatherHeaderBinding
import com.lab.esh1n.weather.weather.model.HeaderHourWeatherModel
import com.lab.esh1n.weather.weather.model.HourWeatherModel
import com.lab.esh1n.weather.weather.model.SimpleHourWeatherModel


class HourWeatherAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = listOf<HourWeatherModel>()

    fun setItems(list: List<HourWeatherModel>) {
        this.items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ITEM_HEADER) {
            val view = parent.inflate(R.layout.item_hour_weather_header)
            return VHHeader(view)
        }
        return VHSimpleItem(parent.inflate(R.layout.item_hour_weather))
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is HeaderHourWeatherModel) {
            ITEM_HEADER
        } else {
            ITEM_SIMPLE
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is VHSimpleItem) {
            holder.bind(items[position] as SimpleHourWeatherModel)
        } else if (holder is VHHeader) {
            holder.bind(items[position] as HeaderHourWeatherModel)
        }
    }

    class VHSimpleItem(view: View) : RecyclerView.ViewHolder(view) {

        private val binding: ItemHourWeatherBinding? = DataBindingUtil.bind(itemView)

        init {
            view.setOnClickListener {
                it.isSelected = !it.isSelected
            }
        }

        fun bind(item: SimpleHourWeatherModel) {
            binding?.let {
                it.weather = item
                it.executePendingBindings()
            }
        }

    }

    class VHHeader(view: View) : RecyclerView.ViewHolder(view) {

        private val binding: ItemHourWeatherHeaderBinding? = DataBindingUtil.bind(itemView)

        fun bind(item: HeaderHourWeatherModel) {
            binding?.let {
                it.header = item
                it.executePendingBindings()
            }
        }

    }

    companion object {
        const val ITEM_HEADER = 0;
        const val ITEM_SIMPLE = 1;
    }
}
