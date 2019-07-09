package com.lab.esh1n.weather.weather

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.base.BaseToolbarActivity
import com.lab.esh1n.weather.utils.addSingleFragmentToContainer
import com.lab.esh1n.weather.utils.replaceFragment
import com.lab.esh1n.weather.utils.setABTitle
import com.lab.esh1n.weather.weather.fragment.CurrentPlaceFragment
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by esh1n on 3/16/18.
 */

class WeatherActivity : BaseToolbarActivity() {


    override val contentViewResourceId = R.layout.activity_main

    private var selectedItem: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addSingleFragmentToContainer(CurrentPlaceFragment.newInstance())
        setupBottomNavigation()
        initFragmentTransactionsListener()

        val restoredMenu = getSelectedFragmentMenuId(savedInstanceState)
        bottom_navigation.selectedItemId = restoredMenu.itemId
    }


    private fun setupBottomNavigation() {
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
            replaceFragment(fragment, name)
            setABTitle(getTitleByTab(this))
        }
    }

    private fun getTitleByTab(tab: MainFragmentTab): CharSequence {
        val titleRes = when (tab) {
            MainFragmentTab.CURRENT_PLACE -> R.string.menu_current_place
            MainFragmentTab.ALL_PLACES -> R.string.menu_destinations_on_earth
            MainFragmentTab.SETTINGS -> R.string.menu_settings
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
        supportFragmentManager.addOnBackStackChangedListener { this.processFragmentsSwitching() }
    }

    private fun processFragmentsSwitching() {

        supportFragmentManager.let {
            val actionBar = supportActionBar
            val isInRootFragment = it.backStackEntryCount == 0
            actionBar?.setDisplayHomeAsUpEnabled(!isInRootFragment)
            if (isInRootFragment) {
                updateTitleForRootFragment(actionBar)
            }
        }

    }

    private fun updateTitleForRootFragment(actionBar: ActionBar?) {
        actionBar?.let {
            val titleForRootFragment = getTitleByTab(getTabByMenu(bottom_navigation.selectedItemId))
            it.title = titleForRootFragment
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SELECTED_ITEM, selectedItem)
        super.onSaveInstanceState(outState)
    }

    public fun setCurrentWeather() {
        bottom_navigation.selectedItemId = R.id.menu_current_city
    }

    companion object {
        private const val SELECTED_ITEM = "arg_selected_item"
    }
}