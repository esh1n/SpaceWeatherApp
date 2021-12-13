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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lab.esh1n.weather.R
import kotlin.random.Random

/**
 * Stateless component that is responsible for the entire todo screen.
 *
 * @param items (state) list of [TodoItem] to display
 * @param onAddItem (event) request an item be added
 * @param onRemoveItem (event) request an item be removed
 */
@Composable
fun FavouritesScreen(
    items: List<TodoItem>,
    onItemClicked: (TodoItem) -> Unit,
    onFavIconItemChange: (TodoItem) -> Unit,
    onGoToSearchPlace: () -> Unit
) {
    Column {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(top = 8.dp)
        ) {
            items(items = items) { todo ->
                FavouritePlaceRow(
                    todo,
                    { onItemClicked(it) },
                    { onFavIconItemChange(it) },
                    Modifier.fillParentMaxWidth()
                )
            }
        }
        // For quick testing, a random item generator button
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
    todo: TodoItem,
    onItemClicked: (TodoItem) -> Unit,
    onFavoriteIconItemClicked: (TodoItem) -> Unit,
    modifier: Modifier = Modifier,
    iconAlpha: Float = remember(todo.id) { randomTint() }
) {
    Row(
        modifier = modifier
            .clickable { onItemClicked(todo) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(todo.task)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(todo.task)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = todo.icon.imageVector,
                tint = LocalContentColor.current.copy(alpha = iconAlpha),
                contentDescription = stringResource(id = todo.icon.contentDescription)
            )
        }
        val favouriteIcon = if (todo.favourite) TodoFavIcon.Favourite else TodoFavIcon.UnFavourite
        IconButton(onClick = { onFavoriteIconItemClicked(todo) }) {
            Icon(
                imageVector = favouriteIcon.imageVector,
                contentDescription = stringResource(id = favouriteIcon.contentDescription),
            )
        }
    }
}

private fun randomTint(): Float {
    return Random.nextFloat().coerceIn(0.3f, 0.9f)
}

@Preview
@Composable
fun PreviewTodoScreen() {
    val items = listOf(
        TodoItem("Learn compose", TodoIcon.Event),
        TodoItem("Take the codelab"),
        TodoItem("Apply state", TodoIcon.Done),
        TodoItem("Build dynamic UIs", TodoIcon.Square)
    )
    FavouritesScreen(items, {}, {}, {})
}

