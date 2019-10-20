package com.lab.esh1n.weather.weather.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.utils.getImage
import com.lab.esh1n.weather.weather.model.HourWeatherModel


class HourWeatherAdapter : RecyclerView.Adapter<HourWeatherAdapter.VH>() {

    private var items = listOf<HourWeatherModel>()

    fun setItems(list: List<HourWeatherModel>) {
        this.items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_hour_weather,
                        parent,
                        false
                )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    class VH(view: View) : RecyclerView.ViewHolder(view) {

        private val tvTime: TextView = view.findViewById(R.id.tv_hour)
        private val ivHourWeather: ImageView = view.findViewById(R.id.iv_hour_weather)
        private val tvTemperature: TextView = view.findViewById(R.id.tv_temperature)

        init {
            view.setOnClickListener {
                it.isSelected = !it.isSelected
            }
        }

        fun bind(item: HourWeatherModel) {
            tvTime.text = item.time
            val preparedIconId = itemView.context.getImage(item.iconId, "ic_")
            ivHourWeather.setImageResource(preparedIconId)
            tvTemperature.text = itemView.context.getString(R.string.text_temperature_celsius_str_value, item.description)
        }

    }
}
