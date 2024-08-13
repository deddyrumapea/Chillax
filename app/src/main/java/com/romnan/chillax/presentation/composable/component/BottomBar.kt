package com.romnan.chillax.presentation.composable.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.romnan.chillax.presentation.composable.destinations.Destination
import com.romnan.chillax.presentation.model.BottomBarDestination

@Composable
fun BottomBar(
    currentDestination: @Composable () -> Destination,
    onClickItem: (BottomBarDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .height(56.dp)
            .selectableGroup(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        BottomBarDestination.entries.forEach { destination: BottomBarDestination ->
            val isSelected = currentDestination() == destination.direction

            BottomNavigationItem(
                selected = isSelected,
                onClick = { onClickItem(destination) },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = stringResource(destination.label),
                        tint = if (isSelected) MaterialTheme.colors.primary
                        else MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                    )
                },
                label = {
                    Text(
                        text = stringResource(destination.label),
                        color = if (isSelected) MaterialTheme.colors.primary
                        else MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                    )
                },
            )
        }
    }
}