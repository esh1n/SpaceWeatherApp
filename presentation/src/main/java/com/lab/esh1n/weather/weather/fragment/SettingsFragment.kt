package com.lab.esh1n.weather.weather.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esh1n.core_android.ui.fragment.BaseVMFragment
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.weather.adapter.SettingsAdapter
import com.lab.esh1n.weather.weather.model.SettingsModel
import com.lab.esh1n.weather.weather.viewmodel.SettingsViewModel

class SettingsFragment : BaseVMFragment<SettingsViewModel>() {

    override val viewModelClass = SettingsViewModel::class.java

    override val layoutResource: Int = R.layout.fragment_settings

    private lateinit var settingsAdapter: SettingsAdapter

    private var settingsRecyclerView: RecyclerView? = null

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun setupView(rootView: View, savedInstanceState: Bundle?) {
        super.setupView(rootView, savedInstanceState)
        settingsAdapter = SettingsAdapter(requireActivity(), this::settingClick)
        settingsRecyclerView = rootView.findViewById(R.id.list_settings)
        settingsRecyclerView?.run {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            adapter = settingsAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.settings.observe(viewLifecycleOwner, { settings ->
            settingsAdapter.setSettings(settings)
        })
        viewModel.loadSettings()
    }

    private fun settingClick(settingsModel: SettingsModel) {
        Toast.makeText(requireActivity(), settingsModel.title, Toast.LENGTH_SHORT).show()
    }
}