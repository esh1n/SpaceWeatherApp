package com.lab.esh1n.weather.weather.favourite

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavouriteViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<List<FavouriteItem>> = MutableStateFlow(
        listOf(
            FavouriteItem("Learn compose", "12%", TodoIcon.Event),
            FavouriteItem("Take the codelab", "12%", favourite = true),
            FavouriteItem("Apply state", "12%", TodoIcon.Done),
            FavouriteItem("Build dynamic UIs", "12%", TodoIcon.Square, favourite = true)
        )
    )

    val uiState = _uiState.asStateFlow()

    fun onItemClicked(item: FavouriteItem) {

    }

    fun onFavIconItemChange(item: FavouriteItem) {

    }
}