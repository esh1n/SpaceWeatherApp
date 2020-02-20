package com.lab.esh1n.weather.weather.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esh1n.core_android.error.ErrorModel
import com.esh1n.core_android.ui.fragment.BaseVMFragment
import com.esh1n.core_android.ui.viewmodel.BaseObserver
import com.esh1n.utils_android.ui.SnackbarBuilder
import com.google.firebase.crashlytics.FirebaseCrashlytics
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
        settingsAdapter = SettingsAdapter(this::settingClick)
        settingsRecyclerView = rootView.findViewById(R.id.list_settings)
        settingsRecyclerView?.let {
            it.layoutManager = LinearLayoutManager(activity)
            it.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            it.setHasFixedSize(true)
            it.adapter = settingsAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.settings.observe(viewLifecycleOwner, object : BaseObserver<List<SettingsModel>>() {
            override fun onData(data: List<SettingsModel>?) {
                data?.let {
                    settingsAdapter.setSettings(data)
                }
            }

            override fun onError(error: ErrorModel?) {
                SnackbarBuilder.buildErrorSnack(requireView(), error?.message ?: "").show()
            }

        })
        viewModel.loadSettings()
    }

    private fun settingClick(settingsModel: SettingsModel) {
        Toast.makeText(requireActivity(), "${settingsModel.title} ${settingsModel.value}", Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        FirebaseCrashlytics.getInstance().log("Settings opened")
    }
}