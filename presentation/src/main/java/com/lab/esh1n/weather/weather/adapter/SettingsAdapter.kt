package com.lab.esh1n.weather.weather.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.esh1n.utils_android.ui.inflate
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.databinding.ItemSettingsGroupHeaderBinding
import com.lab.esh1n.weather.databinding.ItemSettingsTextBinding
import com.lab.esh1n.weather.weather.model.HeaderSettingModel
import com.lab.esh1n.weather.weather.model.SettingsModel
import com.lab.esh1n.weather.weather.model.TextSettingModel
import com.lab.esh1n.weather.weather.viewmodel.SettingsViewModel


class SettingsAdapter(context: Context, private val settingsClick: (SettingsModel) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private var settings: List<SettingsModel>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val view = parent.inflate(R.layout.item_settings_group_header)
                VHHeader(view)
            }
            TYPE_ITEM -> {
                val itemSettingTextBinding = ItemSettingsTextBinding.inflate(inflater, parent, false)
                VHItem(itemSettingTextBinding)
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


    internal inner class VHItem(private val binding: ItemSettingsTextBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        override fun onClick(v: View?) {
            val setting = settings?.get(adapterPosition)
            setting?.let {
                settingsClick(setting)
            }
        }

        init {
            binding.root.setOnClickListener(this)
        }

        fun populate(setting: TextSettingModel) {
            binding.tvTitle.setText(getStringResByTitleKey(setting.title))
            binding.tvValue.text = setting.value
        }

        private fun getStringResByTitleKey(key: String): Int {
            return if (key == SettingsViewModel.KEY_LANGUAGE) {
                R.string.text_settings_language
            } else {
                R.string.app_name
            }
        }
    }

    internal inner class VHHeader(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemSettingsGroupHeaderBinding = ItemSettingsGroupHeaderBinding.bind(itemView)

        fun populate(header: HeaderSettingModel) {
            binding.tvHeader.setText(getStringResByTitleKey(header.title))
        }

        private fun getStringResByTitleKey(key: String): Int {
            return if (key == SettingsViewModel.KEY_MAIN_HEADER) {
                R.string.text_settings_header
            } else {
                R.string.app_name
            }
        }
    }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }
}
