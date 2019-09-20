package com.lab.esh1n.weather.weather.adapter


import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.esh1n.utils_android.ui.inflate
import com.lab.esh1n.weather.databinding.ItemDayOverrallWeatherBinding
import com.lab.esh1n.weather.databinding.ItemHeaderCurrentWeatherBinding
import com.lab.esh1n.weather.weather.model.*


class CurrentWeatherAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    internal inner class VHHeader(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemHeaderCurrentWeatherBinding? = DataBindingUtil.bind(itemView);

        fun populate(weatherModel: CurrentWeatherModel) {
            binding?.let {
                it.weather = weatherModel
                it.viewContent.background = prepareWeatherGradient(WeatherBackgroundModel())
                it.executePendingBindings()
            }
        }
    }

    private fun prepareWeatherGradient(weatherBackgroundModel: WeatherBackgroundModel): GradientDrawable {
        val colors = WeatherBackgroundUtil.getGradientBackgroundColors(weatherBackgroundModel)
        val gd = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, colors)
        gd.cornerRadius = 45f
        return gd

    }

    internal inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemDayOverrallWeatherBinding? = DataBindingUtil.bind(itemView)

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
