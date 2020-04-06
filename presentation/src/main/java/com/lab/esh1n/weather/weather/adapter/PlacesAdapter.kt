package com.lab.esh1n.weather.weather.adapter

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.esh1n.utils_android.ui.inflate
import com.lab.esh1n.data.cache.entity.PlaceWithCurrentWeatherEntry
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.databinding.ItemPlaceBinding
import com.lab.esh1n.weather.weather.model.PlaceModel
import com.lab.esh1n.weather.weather.model.WeatherBackgroundUtil.Companion.prepareWeatherGradient
import kotlin.reflect.KProperty0


class PlacesAdapter(private val mClickHandler: IPlaceClickable, private val placeWeatherMapper: KProperty0<(PlaceWithCurrentWeatherEntry) -> PlaceModel>) :
        PagedListAdapter<PlaceWithCurrentWeatherEntry, PlacesAdapter.ViewHolder>(DIFF_CALLBACK) {



    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = viewGroup.inflate(R.layout.item_place)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val place = getItem(position)
        if (place != null) {
            viewHolder.bindTo(placeWeatherMapper.get().invoke(place))
        } else {
            viewHolder.clear()
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private var itemPlaceBinding: ItemPlaceBinding? = DataBindingUtil.bind(view)

        init {
            view.setOnClickListener(this)
            view.findViewById<View>(R.id.iv_place_actions).setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val adapterPosition = adapterPosition
            val placeModel = getItem(adapterPosition)
            placeModel?.let {
                if (v.id == R.id.iv_place_actions) {
                    mClickHandler.onPlaceOptions(it.id)
                } else {
                    mClickHandler.onPlaceClick(it.id, it.placeName)
                }
            }


        }

        fun bindTo(placeWeather: PlaceModel) {
            itemPlaceBinding?.let {
                it.place = placeWeather
                it.rootContainer.background = prepareWeatherGradient(it.root.context, placeWeather.weatherBackgroundModel)
                it.executePendingBindings()
                // binding.ivAvatar.loadCircleImage(eventModel.actorAvatar)
            }
        }

        fun clear() {
            itemPlaceBinding = null
        }
    }

    interface IPlaceClickable {
        fun onPlaceClick(placeId: Int, placeName: String)
        fun onPlaceOptions(placeId: Int)
    }

    companion object {

        val DIFF_CALLBACK: DiffUtil.ItemCallback<PlaceWithCurrentWeatherEntry> = object : DiffUtil.ItemCallback<PlaceWithCurrentWeatherEntry>() {
            override fun areItemsTheSame(oldItem: PlaceWithCurrentWeatherEntry, newItem: PlaceWithCurrentWeatherEntry): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PlaceWithCurrentWeatherEntry, newItem: PlaceWithCurrentWeatherEntry): Boolean {
                return oldItem == newItem
            }
        }
    }
}