package com.lab.esh1n.weather.weather.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.esh1n.core_android.ui.fragment.BaseVMFragment
import com.esh1n.core_android.ui.setTitle
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.databinding.FragmentWeathersHostBinding
import com.lab.esh1n.weather.utils.viewLifecycle
import com.lab.esh1n.weather.weather.MainFragmentTab
import com.lab.esh1n.weather.weather.viewmodel.EmptyVM


class WeatherHostFragment : BaseVMFragment<EmptyVM>() {

    override val viewModelClass = EmptyVM::class.java

    private var selectedItem: Int = 0

    override val layoutResource: Int = R.layout.fragment_weathers_host

    private var activeFragment: Fragment? = null

    private var binding: FragmentWeathersHostBinding by viewLifecycle()

    companion object {
        private const val SELECTED_ITEM = "arg_selected_item"
        fun newInstance() = WeatherHostFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFragments()

    }

    private fun initFragments() {
        MainFragmentTab.values().forEach { tab ->
            val fragment = tab.fragment
            childFragmentManager
                    .beginTransaction()
                    .add(R.id.container_fragment, fragment, tab.name)
                    .hide(fragment)
                    .commit()
        }
        childFragmentManager.executePendingTransactions()


    }

    override fun setupView(rootView: View, savedInstanceState: Bundle?) {
        super.setupView(rootView, savedInstanceState)
        binding = FragmentWeathersHostBinding.bind(rootView)
        setupBottomNavigation()
        initFragmentTransactionsListener()
        val restoredMenu = getSelectedFragmentMenuId(savedInstanceState)
        binding.navigation.selectedItemId = restoredMenu.itemId
    }


    private fun setupBottomNavigation() {
        binding.navigation.menu.clear() //clear old inflated items.
        binding.navigation.inflateMenu(R.menu.bottom_nav_items);
        binding.navigation.setOnNavigationItemSelectedListener { item ->
            selectFragment(item)
            true
        }
    }

    private fun selectFragment(menuItem: MenuItem) {
        val menuId = menuItem.itemId
        selectedItem = menuId
        updateBottomMenuCheckedItem(menuId)

        with(getTabByMenu(menuId)) {
            replaceFragmentByAttach(this)
            setTitle(getTitleByTab(this))
        }
    }

    private fun replaceFragmentByAttach(tab: MainFragmentTab) {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        val curFrag = childFragmentManager.primaryNavigationFragment
        if (curFrag != null) {
            fragmentTransaction.detach(curFrag)
        }

        val fragment = childFragmentManager.findFragmentByTag(tab.name)!!

        fragmentTransaction.attach(fragment).show(fragment)

        fragmentTransaction.setPrimaryNavigationFragment(fragment)
        fragmentTransaction.setReorderingAllowed(true)
        fragmentTransaction.commitNowAllowingStateLoss()
    }

    private fun replaceFragmentByShowHide(tab: MainFragmentTab) {
        val fragment = childFragmentManager.findFragmentByTag(tab.name)!!

        val transaction = childFragmentManager.beginTransaction()
        activeFragment?.let {
            transaction.hide(it)
        }
        transaction.show(fragment).commit()
        activeFragment = fragment
    }

    private fun getTitleByTab(tab: MainFragmentTab): CharSequence {

        val titleRes = when (tab) {
            MainFragmentTab.ALL_PLACES -> R.string.menu_destinations_on_earth
            MainFragmentTab.SETTINGS -> R.string.menu_settings
            MainFragmentTab.CURRENT_PLACE -> {
                val currentFragment = childFragmentManager.findFragmentByTag(tab.name) as CurrentPlaceFragment
                return currentFragment.getTitle()
            }
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
        val menu = binding.navigation.menu
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            menuItem.isChecked = menuItem.itemId == checkedFragmentId
        }
    }

    private fun getSelectedFragmentMenuId(savedInstance: Bundle?): MenuItem {
        val isSavedDataExist = savedInstance != null
        return if (isSavedDataExist) {
            selectedItem = savedInstance?.getInt(SELECTED_ITEM, 0) ?: 0
            binding.navigation.menu.findItem(selectedItem)
        } else {
            val checkedItem = binding.navigation.selectedItemId
            binding.navigation.menu.findItem(checkedItem)
        }
    }

    private fun initFragmentTransactionsListener() {
        parentFragmentManager.addOnBackStackChangedListener {
            this.processFragmentsSwitching()
        }
    }

    private fun processFragmentsSwitching() {

        parentFragmentManager.let {
            val isInRootFragment = it.backStackEntryCount == 0
            if (isInRootFragment) {
                updateTitleForRootFragment()
            }
        }

    }

    private fun updateTitleForRootFragment() {
        val titleForRootFragment = getTitleByTab(getTabByMenu(binding.navigation.selectedItemId))
        setTitle(titleForRootFragment)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SELECTED_ITEM, selectedItem)
        super.onSaveInstanceState(outState)
    }

    fun setCurrentWeather() {
        binding.navigation.selectedItemId = R.id.menu_current_city
    }


}