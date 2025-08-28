package com.romnan.chillax.presentation.composable.component

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.romnan.chillax.presentation.composable.destinations.Destination
import com.romnan.chillax.presentation.model.BottomBarDestination

@Composable
fun BottomBar(
    currentDestination: @Composable () -> Destination,
    onClickItem: (BottomBarDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier) {
        BottomBarDestination.entries.forEach { destination: BottomBarDestination ->
            val isSelected = currentDestination() == destination.direction

            NavigationBarItem(
                selected = isSelected,
                onClick = { onClickItem(destination) },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = stringResource(destination.label),
                    )
                },
                label = { Text(text = stringResource(destination.label)) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            )
        }
    }
}