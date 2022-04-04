package com.lab.esh1n.weather.presentation


import androidx.fragment.app.Fragment
import com.lab.esh1n.weather.presentation.currentplace.CurrentPlaceFragment
import com.lab.esh1n.weather.presentation.favourite.FavouritePlacesFragment
import com.lab.esh1n.weather.presentation.settings.SettingsFragment

enum class MainFragmentTab(val value: Int) {
    CURRENT_PLACE(0), ALL_PLACES(1), SETTINGS(2);

    val fragment: Fragment
        get() {
            return when (this) {
                CURRENT_PLACE -> CurrentPlaceFragment.newInstance()
                ALL_PLACES -> FavouritePlacesFragment()
                SETTINGS -> SettingsFragment()
            }
        }

    companion object {

        fun getByPosition(position: Int): MainFragmentTab {
            for (tab in values()) {
                if (tab.value == position) {
                    return tab
                }
            }
            throw IllegalArgumentException("No such tab")
        }
    }
}