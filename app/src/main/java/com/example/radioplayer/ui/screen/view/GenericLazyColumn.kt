package com.example.radioplayer.ui.screen.view

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.radioplayer.ui.theme.MainTheme

@Composable
fun <T> GenericLazyColumn(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MainTheme.colors.primaryBackground,
    list: List<T>,
    itemHolder: (@Composable (data: T) -> Unit)
) {
    Surface(color = backgroundColor) {
        LazyColumn(modifier = modifier) {
            items(items = list, key = { it.hashCode() }) {
                itemData -> itemHolder(itemData)
            }
        }
    }
}