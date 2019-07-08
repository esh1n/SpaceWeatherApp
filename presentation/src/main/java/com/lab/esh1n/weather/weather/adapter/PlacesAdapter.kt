package com.lab.esh1n.weather.weather.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.utils.inflate
import com.lab.esh1n.weather.weather.model.PlaceWeather


class PlacesAdapter(private val mClickHandler: (PlaceWeather) -> Unit) :
        RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {

    private var places: List<PlaceWeather>? = null


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = viewGroup.context.inflate(R.layout.item_place, viewGroup)
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

    fun swapCities(newTags: List<PlaceWeather>) {
        if (places == null) {
            this.places = newTags
            notifyDataSetChanged()
        } else {
            val result = DiffUtil.calculateDiff(DiffCallback(places!!, newTags))
            places = newTags
            result.dispatchUpdatesTo(this)
        }
    }


    private inner class DiffCallback(private val oldBrands: List<PlaceWeather>, private val newBrands: List<PlaceWeather>) :
            DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldBrands.size
        }

        override fun getNewListSize(): Int {
            return newBrands.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val cityName = oldBrands[oldItemPosition].cityName
            val cityName1 = newBrands[newItemPosition].cityName
            return cityName == cityName1
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldBrand = oldBrands[oldItemPosition]
            val newBrand = newBrands[newItemPosition]
            return oldBrand == newBrand
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private var placeNameTextView: TextView? = view.findViewById(R.id.tv_city_name)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val adapterPosition = adapterPosition
            val brandModel = places?.get(adapterPosition)
            brandModel?.let {
                mClickHandler.invoke(it)
            }

        }

        fun bindTo(placeWeather: PlaceWeather) {
            placeNameTextView?.text = placeWeather.cityName
        }

        fun clear() {
            placeNameTextView = null
        }
    }
}