package com.lab.esh1n.weather.weather.adapter


import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.esh1n.utils_android.ui.inflate
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.databinding.ItemDayOverrallWeatherBinding
import com.lab.esh1n.weather.databinding.ItemHeaderCurrentWeatherBinding
import com.lab.esh1n.weather.weather.model.CurrentWeatherModel
import com.lab.esh1n.weather.weather.model.DayWeatherModel
import com.lab.esh1n.weather.weather.model.WeatherBackgroundModel
import com.lab.esh1n.weather.weather.model.WeatherBackgroundUtil.Companion.prepareWeatherGradient
import com.lab.esh1n.weather.weather.model.WeatherModel


class CurrentWeatherAdapter(private val cityDayForecastClick: (WeatherModel) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var weathers: List<WeatherModel>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_HEADER) {
            val view = parent.inflate(R.layout.item_header_current_weather)
            return VHHeader(view)
        } else {
            val view = parent.inflate(R.layout.item_day_overrall_weather)
            return VHItem(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is VHItem) {
            val dayWeatherModel = getItem(position) as DayWeatherModel
            holder.populate(dayWeatherModel)
        } else if (holder is VHHeader) {
            val currentWeather = getItem(position) as CurrentWeatherModel
            holder.populate(currentWeather)
        }
    }

    override fun getItemCount(): Int {
        return weathers?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (isPositionHeader(position)) TYPE_HEADER else TYPE_ITEM

    }

    private fun isPositionHeader(position: Int): Boolean {
        return getItem(position) is CurrentWeatherModel
    }

    private fun getItem(position: Int): WeatherModel? {
        return weathers?.get(position)
    }

    fun swapWeathers(newWeathers: List<WeatherModel>) {
        if (weathers.isNullOrEmpty()) {
            this.weathers = newWeathers
            notifyDataSetChanged()
        } else {
            val result = DiffUtil.calculateDiff(DiffCallback(weathers!!, newWeathers))
            this.weathers = newWeathers
            result.dispatchUpdatesTo(this)
        }
    }

    private inner class DiffCallback(private val oldBrands: List<WeatherModel>, private val newBrands: List<WeatherModel>) :
            DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldBrands.size
        }

        override fun getNewListSize(): Int {
            return newBrands.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val humanDate = oldBrands[oldItemPosition].humanDate
            val humanDate1 = newBrands[newItemPosition].humanDate
            return humanDate == humanDate1
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldBrand = oldBrands[oldItemPosition]
            val newBrand = newBrands[newItemPosition]
            return oldBrand.humanDate == newBrand.humanDate
                    && oldBrand.tempMax == newBrand.tempMax
                    && oldBrand.tempMin == newBrand.tempMin
                    && oldBrand.iconId == newBrand.iconId
        }
    }

    internal inner class VHHeader(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        override fun onClick(v: View?) {
            val weatherModel = weathers?.get(adapterPosition)
            weatherModel?.let {
                cityDayForecastClick(weatherModel)
            }
        }

        var binding: ItemHeaderCurrentWeatherBinding? = DataBindingUtil.bind(itemView)

        init {
            itemView.setOnClickListener(this)
        }

        fun populate(weatherModel: CurrentWeatherModel) {
            binding?.let {
                it.weather = weatherModel
                val backgroundModel = WeatherBackgroundModel(weatherModel.iconId, weatherModel.isDay, weatherModel.hour24Format, weatherModel.cloudiness, weatherModel.rain, weatherModel.snow)
                it.viewContent.background = prepareWeatherGradient(it.root.context, backgroundModel)
                it.executePendingBindings()
            }
        }
    }


    internal inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var binding: ItemDayOverrallWeatherBinding? = DataBindingUtil.bind(itemView)

        override fun onClick(v: View?) {
            val weatherModel = weathers?.get(adapterPosition)
            weatherModel?.let {
                cityDayForecastClick(weatherModel)
            }
        }


        init {
            itemView.setOnClickListener(this)
        }

        fun populate(weatherModel: DayWeatherModel) {
            binding?.let {
                it.weather = weatherModel
                it.executePendingBindings()
            }
        }
    }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }
}
