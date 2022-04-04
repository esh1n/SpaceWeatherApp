package com.lab.esh1n.weather.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SectionSpacer(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(48.dp)
            .background(MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
    )
}