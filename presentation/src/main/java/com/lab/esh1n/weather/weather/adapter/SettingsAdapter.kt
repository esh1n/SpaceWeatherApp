package com.lab.esh1n.weather.weather.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.esh1n.utils_android.ui.inflate
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.weather.model.HeaderSettingModel
import com.lab.esh1n.weather.weather.model.SettingsModel
import com.lab.esh1n.weather.weather.model.TextSettingModel


class SettingsAdapter(private val settingsClick: (SettingsModel) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var settings: List<SettingsModel>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val view = parent.inflate(R.layout.item_settings_group_header)
                VHHeader(view)
            }
            TYPE_ITEM -> {
                val view = parent.inflate(R.layout.item_settings_text)
                VHItem(view)
            }
            else -> {
                val view = parent.inflate(R.layout.item_settings_text)
                VHHeader(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is VHItem) {
            val textSetting = getItem(position) as TextSettingModel
            holder.populate(textSetting)
        } else if (holder is VHHeader) {
            val header = getItem(position) as HeaderSettingModel
            holder.populate(header)
        }
    }

    override fun getItemCount(): Int {
        return settings?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            isPositionHeader(position) -> TYPE_HEADER
            else -> TYPE_ITEM
        }
    }

    private fun isPositionHeader(position: Int): Boolean {
        return getItem(position) is HeaderSettingModel
    }

    private fun getItem(position: Int): SettingsModel? {
        return if (settings.isNullOrEmpty() || position == settings!!.size)
            null
        else settings?.get(position)
    }

    fun setSettings(newWeathers: List<SettingsModel>) {
        this.settings = newWeathers
        notifyDataSetChanged()
    }


    internal inner class VHItem(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var txtTitle: TextView? = itemView.findViewById(R.id.tv_title)
        var txtValue: TextView? = itemView.findViewById(R.id.tv_value)
        override fun onClick(v: View?) {
            val setting = settings?.get(adapterPosition)
            setting?.let {
                settingsClick(setting)
            }
        }

        init {
            itemView.setOnClickListener(this)
        }

        fun populate(setting: TextSettingModel) {
            txtTitle?.text = setting.title
            txtValue?.text = setting.value
        }
    }

    internal inner class VHHeader(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView? = itemView.findViewById(R.id.tv_header)

        fun populate(header: HeaderSettingModel) {
            title?.let {
                it.text = header.title
            }
        }
    }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }
}
