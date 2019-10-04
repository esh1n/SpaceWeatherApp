package com.lab.esh1n.weather.weather.adapter


import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.esh1n.utils_android.ui.inflate
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.databinding.ItemDayOverrallWeatherBinding
import com.lab.esh1n.weather.databinding.ItemHeaderCurrentWeatherBinding
import com.lab.esh1n.weather.weather.fragment.CurrentPlaceFragment
import com.lab.esh1n.weather.weather.model.CurrentWeatherModel
import com.lab.esh1n.weather.weather.model.DayWeatherModel
import com.lab.esh1n.weather.weather.model.WeatherBackgroundModel
import com.lab.esh1n.weather.weather.model.WeatherBackgroundUtil.Companion.prepareWeatherGradient
import com.lab.esh1n.weather.weather.model.WeatherModel


class CurrentWeatherAdapter(private val cityDayForecastClick: (WeatherModel) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var weathers: List<WeatherModel>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val view = parent.inflate(R.layout.item_header_current_weather)
                VHHeader(view)
            }
            TYPE_ITEM -> {
                val view = parent.inflate(R.layout.item_day_overrall_weather)
                VHItem(view)
            }
            else -> {
                val bannerLayoutView = parent.inflate(R.layout.banner_ad_container)
                AdViewHolder(bannerLayoutView)
            }
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
        return if (weathers.isNullOrEmpty()) {
            0
        } else {
            weathers!!.size + ADS_BANNER_COUNT
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            isPositionHeader(position) -> TYPE_HEADER
            position == weathers?.size ?: -1 -> TYPE_AD_BANNER
            else -> TYPE_ITEM
        }
    }

    private fun isPositionHeader(position: Int): Boolean {
        return getItem(position) is CurrentWeatherModel
    }

    private fun getItem(position: Int): WeatherModel? {
        return if (weathers.isNullOrEmpty() || position == weathers!!.size)
            null
        else weathers?.get(position)
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

    inner class AdViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        private var adView: AdView? = null

        init {
            initAd(view)
        }

        private fun initAd(view: View) {
            adView = view.findViewById(R.id.adView)
            adView?.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    Log.d(CurrentPlaceFragment.TAG, "onAdLoaded")
                    // Code to be executed when an ad finishes loading.
                }

                override fun onAdFailedToLoad(errorCode: Int) {
                    Log.d(CurrentPlaceFragment.TAG, "onAdFailedToLoad")
                    // Code to be executed when an ad request fails.
                }

                override fun onAdOpened() {
                    Log.d(CurrentPlaceFragment.TAG, "onAdOpened")
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                }

                override fun onAdClicked() {
                    Log.d(CurrentPlaceFragment.TAG, "onAdClicked")
                    // Code to be executed when the user clicks on an ad.
                }

                override fun onAdLeftApplication() {
                    Log.d(CurrentPlaceFragment.TAG, "onAdLeftApplication")
                    // Code to be executed when the user has left the app.
                }

                override fun onAdClosed() {
                    Log.d(CurrentPlaceFragment.TAG, "onAdClosed")
                    // Code to be executed when the user is about to return
                    // to the app after tapping on an ad.
                }
            }
            val adRequest = AdRequest.Builder().build()
            adView?.loadAd(adRequest)
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
        private const val TYPE_AD_BANNER = 2
        private const val ADS_BANNER_COUNT = 1
    }
}
