package com.lab.esh1n.weather.weather.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.esh1n.utils_android.ui.inflate
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.utils.StringResValueProperty
import com.lab.esh1n.weather.utils.convertProperty

abstract class ForecastDaytimesAdapter<T : DaytimeForecastModel> : RecyclerView.Adapter<ForecastDaytimesAdapter.VHItem<T>>() {

    private var items = listOf<T>()

    fun setItems(list: List<T>) {
        this.items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItem<T> {
        return provideVHolder(parent.inflate(getItemViewId()))
    }

    abstract fun provideVHolder(view: View): VHItem<T>

    abstract fun getItemViewId(): Int

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VHItem<T>, position: Int) {
        holder.bind(items[position])
    }

    abstract class VHItem<T : DaytimeForecastModel>(view: View) : RecyclerView.ViewHolder(view) {

        private val tvTitle: TextView? = view.findViewById(R.id.tv_date)

        fun bind(item: T) {
            val title = StringResValueProperty(item.dayTime).convertProperty(itemView.context)
            tvTitle?.text = title
            bindContent(itemView, item)
        }

        abstract fun bindContent(itemView: View, item: T)

    }

}
