package com.lab.esh1n.weather.weather.favourite

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavouriteViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<List<TodoItem>> = MutableStateFlow(
        listOf(
            TodoItem("Learn compose", TodoIcon.Event),
            TodoItem("Take the codelab", favourite = true),
            TodoItem("Apply state", TodoIcon.Done),
            TodoItem("Build dynamic UIs", TodoIcon.Square, favourite = true)
        )
    )

    val uiState = _uiState.asStateFlow()

    fun onItemClicked(item: TodoItem) {

    }

    fun onFavIconItemChange(item: TodoItem) {

    }
}