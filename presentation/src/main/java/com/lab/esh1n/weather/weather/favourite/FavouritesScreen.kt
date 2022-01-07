/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lab.esh1n.weather.weather.favourite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lab.esh1n.weather.R
import com.lab.esh1n.weather.utils.getWeatherStatusImage
import kotlin.random.Random

/**
 * Stateless component that is responsible for the entire todo screen.
 *
 * @param items (state) list of [FavouriteUiModel] to display
 * @param onAddItem (event) request an item be added
 * @param onRemoveItem (event) request an item be removed
 */
@Composable
fun FavouritesScreen(
    uiState: FavouritesUiState,
    onItemClicked: (FavouriteUiModel) -> Unit,
    onFavIconItemChange: (FavouriteUiModel) -> Unit,
    onGoToSearchPlace: () -> Unit
) {
    Column {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(top = 8.dp)
        ) {
            itemsIndexed(items = uiState.favs) { index, todo ->
                FavouritePlaceRow(
                    todo,
                    { onItemClicked(it) },
                    { onFavIconItemChange(it) },
                    Modifier.fillParentMaxWidth()
                )
                if (index < uiState.favs.lastIndex)
                    Divider(color = Color.Black, thickness = 1.dp)
            }
        }
        Button(
            onClick = { onGoToSearchPlace() },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            Text(stringResource(id = R.string.text_open_search_place_screen))
        }
    }
}


@Composable
fun FavouritePlaceRow(
    favourite: FavouriteUiModel,
    onItemClicked: (FavouriteUiModel) -> Unit,
    onFavoriteIconItemClicked: (FavouriteUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .clickable { onItemClicked(favourite) }
        .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(favourite.name)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Text(stringResource(id = R.string.text_temperature_celsius, favourite.temperature))
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(
                        id = LocalContext.current.getWeatherStatusImage(
                            favourite.icon
                        )
                    ),
                    tint = Color.Unspecified,
                    contentDescription = favourite.icon
                )
            }
            val favouriteIcon =
                if (favourite.favourite) TodoFavIcon.Favourite else TodoFavIcon.UnFavourite
            IconButton(onClick = { onFavoriteIconItemClicked(favourite) }) {
                Icon(
                    imageVector = favouriteIcon.imageVector,
                    contentDescription = stringResource(id = favouriteIcon.contentDescription),
                )
            }
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(favourite.description)
    }

}

private fun randomTint(): Float {
    return Random.nextFloat().coerceIn(0.3f, 0.9f)
}

@Preview
@Composable
fun PreviewTodoScreen() {
    val items = listOf(
        FavouriteUiModel("Sochi", "good", 12, "00d"),
        FavouriteUiModel("Voronezh", "good", 12, "00d", favourite = true),
    )
    FavouritesScreen(FavouritesUiState(items), {}, {}, {})
}

