package com.lab.esh1n.weather.weather.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crashlytics.android.Crashlytics
import com.esh1n.core_android.ui.fragment.BaseVMFragment
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.weather.adapter.SettingsAdapter
import com.lab.esh1n.weather.weather.model.HeaderSettingModel
import com.lab.esh1n.weather.weather.model.SettingsModel
import com.lab.esh1n.weather.weather.model.TextSettingModel
import com.lab.esh1n.weather.weather.viewmodel.EmptyVM

class SettingsFragment : BaseVMFragment<EmptyVM>() {

    override val viewModelClass = EmptyVM::class.java

    override val layoutResource: Int = R.layout.fragment_settings

    private lateinit var settingsAdapter: SettingsAdapter

    private var settingsRecyclerView: RecyclerView? = null

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun setupView(rootView: View, savedInstanceState: Bundle?) {
        super.setupView(rootView, savedInstanceState)
        settingsAdapter = SettingsAdapter(this::settingClick)
        settingsRecyclerView = rootView.findViewById(R.id.list_settings)
        settingsRecyclerView?.let {
            it.layoutManager = LinearLayoutManager(activity)
            it.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            it.setHasFixedSize(true)
            it.adapter = settingsAdapter
        }
        settingsAdapter.setSettings(arrayListOf(HeaderSettingModel("Main"), TextSettingModel("Language", "English")))
    }

    private fun settingClick(settingsModel: SettingsModel) {
        Toast.makeText(requireActivity(), "${settingsModel.title} ${settingsModel.value}", Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        Crashlytics.log(Log.DEBUG, "SettingsFragment", "Settings opened")
    }
}