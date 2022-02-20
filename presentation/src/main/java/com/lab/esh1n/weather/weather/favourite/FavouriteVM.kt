package com.lab.esh1n.weather.weather.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lab.esh1n.weather.domain.places.PlacesRepository
import com.lab.esh1n.weather.domain.prefs.TemperatureUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavouriteVM @Inject
constructor(private val placesRepository: PlacesRepository) : ViewModel() {
    private val _uiState: MutableStateFlow<FavouritesUiState> =
        MutableStateFlow(FavouritesUiState())

    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            placesRepository.loadFavouritePlaces().map {
                it.map { place ->
                    FavouriteUiModel(
                        name = place.name,
                        temperature = place.temperature.convertTo(TemperatureUnit.C).toInt(),
                        description = place.description,
                        icon = place.iconId
                    )
                }
            }.collect { updateState { copy(favs = it) } }
        }
    }

    private fun updateState(transform: FavouritesUiState.() -> FavouritesUiState) {
        _uiState.value = transform(uiState.value)
    }

    fun onItemClicked(uiModel: FavouriteUiModel) {

    }

    fun onFavIconItemChange(uiModel: FavouriteUiModel) {

    }
}

data class FavouritesUiState(val favs: List<FavouriteUiModel> = emptyList())