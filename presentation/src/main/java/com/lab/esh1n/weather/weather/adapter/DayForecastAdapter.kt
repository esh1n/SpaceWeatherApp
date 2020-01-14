package com.lab.esh1n.weather.weather.adapter


import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esh1n.utils_android.ui.OrientationAwareRecyclerView
import com.esh1n.utils_android.ui.inflate
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.utils.*
import com.lab.esh1n.weather.weather.fragment.CurrentPlaceFragment


class DayForecastAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var titlesAndWeathers: List<Pair<DayForecastSection, List<DaytimeForecastModel>>>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DayForecastSection.AD.index) {
            val bannerLayoutView = parent.inflate(R.layout.banner_ad_container)
            AdViewHolder(bannerLayoutView)
        } else {
            val view = parent.inflate(R.layout.item_forecast_section_container)
            val vh = when (viewType) {
                DayForecastSection.MAIN.index -> {
                    MainSectionViewHolder(view)
                }
                DayForecastSection.WIND.index -> {
                    WindSectionViewHolder(view)
                }
                DayForecastSection.HUMIDITY.index -> {
                    HumiditySectionViewHolder(view)
                }
                DayForecastSection.PRESSURE.index -> {
                    PressureSectionViewHolder(view)
                }
                else ->
                    WindSectionViewHolder(view)
            }
            vh.onCreated()
            vh
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is AdViewHolder) {
            val weathers = getItem(position) as Pair<DayForecastSection, List<out DaytimeForecastModel>>
            if (holder is MainSectionViewHolder) {
                holder.populate(weathers.first, weathers.second as List<DayOverallForecastModel>)
            } else if (holder is WindSectionViewHolder) {
                holder.populate(weathers.first, weathers.second as List<DayWindForecastModel>)
            } else if (holder is HumiditySectionViewHolder) {
                holder.populate(weathers.first, weathers.second as List<DayHumidityForecastModel>)
            } else if (holder is PressureSectionViewHolder) {
                holder.populate(weathers.first, weathers.second as List<DayPressureForecastModel>)
            }
        }

    }

    override fun getItemCount(): Int {
        return if (titlesAndWeathers.isNullOrEmpty()) {
            0
        } else {
            titlesAndWeathers!!.size + ADS_BANNER_COUNT
        }
    }

    override fun getItemViewType(position: Int): Int {
        val type = getItem(position)?.first ?: DayForecastSection.AD
        return type.index
    }

    private fun getItem(position: Int): Pair<DayForecastSection, List<DaytimeForecastModel>>? {
        return if (titlesAndWeathers.isNullOrEmpty() || position == titlesAndWeathers!!.size)
            null
        else titlesAndWeathers?.get(position)
    }

    fun updateForecastData(newForecastData: List<Pair<DayForecastSection, List<DaytimeForecastModel>>>) {
        this.titlesAndWeathers = newForecastData
        notifyDataSetChanged()
    }


    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is MainSectionViewHolder) {
            holder.onRecycled()
        } else if (holder is WindSectionViewHolder) {
            holder.onRecycled()
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder is MainSectionViewHolder) {
            holder.onDetachedFromWindow()
        } else if (holder is WindSectionViewHolder) {
            holder.onDetachedFromWindow()
        }
    }

    abstract class VHSectionItem<T : DaytimeForecastModel>(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val layoutManager = LinearLayoutManager(
                itemView.context,
                RecyclerView.HORIZONTAL, false)
        private lateinit var forecastAdapter: ForecastDaytimesAdapter<T>

        private val titleContainer = itemView.findViewById<View>(R.id.header_container)
        private val tvTitle = itemView.findViewById<AppCompatTextView>(R.id.tv_daytime)
        private val ivStatus = itemView.findViewById<AppCompatImageView>(R.id.iv_weather_status)
        private val weathersList = itemView.findViewById<OrientationAwareRecyclerView>(R.id.rv_day_time_weathers)

        abstract fun provideForecastAdapter(): ForecastDaytimesAdapter<T>

        fun populate(type: DayForecastSection, forecasts: List<T>) {
            val isMainSection = type == DayForecastSection.MAIN
            titleContainer.visibility = if (isMainSection) View.GONE else View.VISIBLE
            if (!isMainSection) {
                with(itemView.context) {
                    tvTitle.text = StringResValueProperty(type.titleStringRes).convertProperty(itemView.context)
                    ivStatus.setImageResource(getResourceImage(type.iconId))
                }

            }
            forecastAdapter.setItems(forecasts)
        }


        fun onCreated() {
            forecastAdapter = provideForecastAdapter()
            weathersList.let {
                it.adapter = forecastAdapter
                it.layoutManager = layoutManager
                it.addItemDecoration(DividerItemDecoration(it.context, DividerItemDecoration.HORIZONTAL))
                it.setHasFixedSize(true)
            }

        }

        fun onRecycled() {}


        fun onDetachedFromWindow() {}
    }

    class MainSectionViewHolder(itemView: View) : VHSectionItem<DayOverallForecastModel>(itemView) {

        override fun provideForecastAdapter(): ForecastDaytimesAdapter<DayOverallForecastModel> {
            return object : ForecastDaytimesAdapter<DayOverallForecastModel>() {
                override fun provideVHolder(view: View): VHItem<DayOverallForecastModel> {
                    return object : VHItem<DayOverallForecastModel>(view) {
                        override fun bindContent(itemView: View, item: DayOverallForecastModel) {
                            val ivStatus = itemView.findViewById<AppCompatImageView>(R.id.iv_weather_status)
                            val src = itemView.context.getWeatherStatusImage(item.iconId)
                            ivStatus.setImageResource(src)
                            val temperature = item.temperature.convertProperty(itemView.context)
                            val tvTemperature = itemView.findViewById<AppCompatTextView>(R.id.tv_temperature)
                            tvTemperature.text = temperature
                        }

                    }
                }

                override fun getItemViewId() = R.layout.item_day_forecast_main

            }
        }

    }

    class WindSectionViewHolder(itemView: View) : VHSectionItem<DayWindForecastModel>(itemView) {

        override fun provideForecastAdapter(): ForecastDaytimesAdapter<DayWindForecastModel> {
            return object : ForecastDaytimesAdapter<DayWindForecastModel>() {
                override fun provideVHolder(view: View): VHItem<DayWindForecastModel> {
                    return object : VHItem<DayWindForecastModel>(view) {
                        override fun bindContent(itemView: View, item: DayWindForecastModel) {
                            val imageDirection = itemView.findViewById<AppCompatImageView>(R.id.iv_wind)
                            val src = itemView.context.getResourceImage(item.iconId)
                            //  imageDirection.setImageResource(src)
                            imageDirection.rotation = item.windDegree
                            val tvWindSpeed = itemView.findViewById<AppCompatTextView>(R.id.tv_wind_value)
                            tvWindSpeed.text = item.windSpeed.convertProperty(itemView.context)
                            val windDirection = itemView.findViewById<AppCompatTextView>(R.id.tv_wind_direction)
                            windDirection.text = item.windDirection.convertProperty(itemView.context)
                        }

                    }
                }

                override fun getItemViewId() = R.layout.item_day_forecast_wind

            }
        }

    }

    class HumiditySectionViewHolder(itemView: View) : VHSectionItem<DayHumidityForecastModel>(itemView) {

        override fun provideForecastAdapter(): ForecastDaytimesAdapter<DayHumidityForecastModel> {
            return object : ForecastDaytimesAdapter<DayHumidityForecastModel>() {
                override fun provideVHolder(view: View): VHItem<DayHumidityForecastModel> {
                    return object : VHItem<DayHumidityForecastModel>(view) {
                        override fun bindContent(itemView: View, item: DayHumidityForecastModel) {
                            val humidityValue = itemView.findViewById<AppCompatTextView>(R.id.tv_humidity_value)
                            humidityValue.text = item.humidity.convertProperty(itemView.context)
                        }

                    }
                }

                override fun getItemViewId() = R.layout.item_day_forecast_humidity

            }
        }

    }

    class PressureSectionViewHolder(itemView: View) : VHSectionItem<DayPressureForecastModel>(itemView) {

        override fun provideForecastAdapter(): ForecastDaytimesAdapter<DayPressureForecastModel> {
            return object : ForecastDaytimesAdapter<DayPressureForecastModel>() {
                override fun provideVHolder(view: View): VHItem<DayPressureForecastModel> {
                    return object : VHItem<DayPressureForecastModel>(view) {
                        override fun bindContent(itemView: View, item: DayPressureForecastModel) {
                            val windDirection = itemView.findViewById<AppCompatTextView>(R.id.tv_pressure_value)
                            windDirection.text = item.pressure.convertProperty(itemView.context)
                        }

                    }
                }

                override fun getItemViewId() = R.layout.item_day_forecast_pressure

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
                    Log.d("AdTest", " onAdLoaded")
                    adView?.tag = true;

                    // Code to be executed when an ad finishes loading.
                }

                override fun onAdFailedToLoad(errorCode: Int) {
                    Log.d("AdTest", " onAdFailedToLoad")
                    Log.d(CurrentPlaceFragment.TAG, "onAdFailedToLoad")
                    adView?.tag = false; // Set tag false if loading failed
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

            adView?.let {
                val loaded = it.tag != null && it.tag is Boolean && it.tag as Boolean
                Log.d("AdTest", "loaded $loaded ")
                if (!loaded) {
                    val adRequest = AdRequest.Builder().build()
                    it.loadAd(adRequest)
                }
            }


        }
    }


    companion object {
        const val ADS_BANNER_COUNT = 1
    }

}

public enum class DayForecastSection(val index: Int, val titleStringRes: Int, val iconId: String) {
    MAIN(1, R.string.title_forecast_section_stub, "sunset"),
    WIND(2, R.string.title_forecast_section_wind, "wind"),
    PRESSURE(3, R.string.title_forecast_section_pressure, "pressure"),
    SUNSET(4, R.string.title_forecast_section_sunset, "sunset"),
    HUMIDITY(5, R.string.title_forecast_section_humidity, "humidity"),
    AD(6, R.string.title_forecast_section_stub, "sunset")
}

sealed class DaytimeForecastModel(val dayTime: StringResValueProperty)
class DayOverallForecastModel(dayTime: StringResValueProperty, val iconId: String, val temperature: OneValueProperty) : DaytimeForecastModel(dayTime)
class DayWindForecastModel(dayTime: StringResValueProperty, val iconId: String, val windSpeed: OneValueProperty, val windDirection: StringResValueProperty, val windDegree: Float) : DaytimeForecastModel(dayTime)
class DayHumidityForecastModel(val humidity: OneValueProperty, dayTime: StringResValueProperty) : DaytimeForecastModel(dayTime)
class DayPressureForecastModel(val pressure: OneValueProperty, dayTime: StringResValueProperty) : DaytimeForecastModel(dayTime)
