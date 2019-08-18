package com.lab.esh1n.weather.weather.adapter

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.esh1n.utils_android.ui.inflate
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.databinding.ItemPlaceBinding
import com.lab.esh1n.weather.weather.model.PlaceModel


class PlacesAdapter(private val mClickHandler: IPlaceClickable) :
        RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {

    private var places: List<PlaceModel>? = null


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = viewGroup.inflate(R.layout.item_place)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val place = places?.get(position)
        if (place != null) {
            viewHolder.bindTo(place)
        } else {
            viewHolder.clear()
        }
    }

    override fun getItemCount(): Int {
        return places?.size ?: 0
    }

    fun swapCities(newTags: List<PlaceModel>) {
        if (places == null) {
            this.places = newTags
            notifyDataSetChanged()
        } else {
            val result = DiffUtil.calculateDiff(DiffCallback(places!!, newTags))
            places = newTags
            result.dispatchUpdatesTo(this)
        }
    }


    private inner class DiffCallback(private val oldBrands: List<PlaceModel>, private val newBrands: List<PlaceModel>) :
            DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldBrands.size
        }

        override fun getNewListSize(): Int {
            return newBrands.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val cityName = oldBrands[oldItemPosition].name
            val cityName1 = newBrands[newItemPosition].name
            return cityName == cityName1
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldBrand = oldBrands[oldItemPosition]
            val newBrand = newBrands[newItemPosition]
            return oldBrand == newBrand
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
            val placeModel = places?.get(adapterPosition)
            placeModel?.let {
                if (v.id == R.id.iv_place_actions) {
                    mClickHandler.onPlaceOptions(it)
                } else {
                    mClickHandler.onPlaceClick(it)
                }
            }


        }

        fun bindTo(placeWeather: PlaceModel) {
            itemPlaceBinding?.let {
                it.place = placeWeather
                it.executePendingBindings()
                // binding.ivAvatar.loadCircleImage(eventModel.actorAvatar)
            }
        }

        fun clear() {
            itemPlaceBinding = null
        }
    }

    interface IPlaceClickable {
        fun onPlaceClick(placeWeather: PlaceModel)
        fun onPlaceOptions(placeWeather: PlaceModel)
    }
}