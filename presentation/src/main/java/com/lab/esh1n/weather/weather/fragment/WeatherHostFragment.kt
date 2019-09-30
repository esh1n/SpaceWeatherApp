package com.lab.esh1n.weather.weather.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.esh1n.core_android.ui.fragment.BaseVMFragment
import com.esh1n.core_android.ui.replaceFragment
import com.esh1n.core_android.ui.setTitle
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.weather.MainFragmentTab
import com.lab.esh1n.weather.weather.viewmodel.SettingsVM
import kotlinx.android.synthetic.main.fragment_weathers_host.*

class WeatherHostFragment : BaseVMFragment<SettingsVM>() {

    override val viewModelClass = SettingsVM::class.java

    private var selectedItem: Int = 0

    override val layoutResource: Int = R.layout.fragment_weathers_host

    companion object {
        private const val SELECTED_ITEM = "arg_selected_item"
        fun newInstance() = WeatherHostFragment()
    }

    override fun setupView(rootView: View, savedInstanceState: Bundle?) {
        super.setupView(rootView, savedInstanceState)
        setupBottomNavigation()
        initFragmentTransactionsListener()
        val restoredMenu = getSelectedFragmentMenuId(savedInstanceState)
        bottom_navigation.selectedItemId = restoredMenu.itemId
    }


    private fun setupBottomNavigation() {
        bottom_navigation.getMenu().clear(); //clear old inflated items.
        bottom_navigation.inflateMenu(R.menu.bottom_nav_items);
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            selectFragment(item)
            true
        }
    }

    private fun selectFragment(menuItem: MenuItem) {
        val menuId = menuItem.itemId
        selectedItem = menuId
        updateBottomMenuCheckedItem(menuId)

        with(getTabByMenu(menuId)) {
            childFragmentManager.replaceFragment(fragment, name)
            setTitle(getTitleByTab(this))
        }
    }

    private fun getTitleByTab(tab: MainFragmentTab): CharSequence {
        if (tab == MainFragmentTab.CURRENT_PLACE) {
            return ""
        }
        val titleRes = when (tab) {
            MainFragmentTab.ALL_PLACES -> R.string.menu_destinations_on_earth
            MainFragmentTab.SETTINGS -> R.string.menu_settings
            else -> R.string.app_name
        }
        return getString(titleRes)
    }

    private fun getTabByMenu(menu: Int): MainFragmentTab {
        return when (menu) {
            R.id.menu_current_city -> MainFragmentTab.CURRENT_PLACE
            R.id.menu_all_cities -> MainFragmentTab.ALL_PLACES
            R.id.menu_settings -> MainFragmentTab.SETTINGS
            else -> MainFragmentTab.SETTINGS
        }
    }

    private fun updateBottomMenuCheckedItem(checkedFragmentId: Int) {
        val menu = bottom_navigation.menu
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            menuItem.isChecked = menuItem.itemId == checkedFragmentId
        }
    }

    private fun getSelectedFragmentMenuId(savedInstance: Bundle?): MenuItem {
        val isSavedDataExist = savedInstance != null
        return if (isSavedDataExist) {
            selectedItem = savedInstance?.getInt(SELECTED_ITEM, 0) ?: 0
            bottom_navigation.menu.findItem(selectedItem)
        } else {
            val checkedItem = bottom_navigation!!.selectedItemId
            bottom_navigation.menu.findItem(checkedItem)
        }
    }

    private fun initFragmentTransactionsListener() {
        childFragmentManager.addOnBackStackChangedListener { this.processFragmentsSwitching() }
    }

    private fun processFragmentsSwitching() {

        childFragmentManager.let {
            val isInRootFragment = it.backStackEntryCount == 0
            //TODO move it to main activity
            //requireActivity().actionBar?.setDisplayHomeAsUpEnabled(!isInRootFragment)
            if (isInRootFragment) {
                updateTitleForRootFragment()
            }
        }

    }

    private fun updateTitleForRootFragment() {
        val titleForRootFragment = getTitleByTab(getTabByMenu(bottom_navigation.selectedItemId))
        setTitle(titleForRootFragment)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SELECTED_ITEM, selectedItem)
        super.onSaveInstanceState(outState)
    }

    fun setCurrentWeather() {
        bottom_navigation.selectedItemId = R.id.menu_current_city
    }


}