package com.romnan.chillax.presentation.composable.settings.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.romnan.chillax.presentation.composable.theme.spacing

@Composable
fun SwitchPreference(
    icon: () -> ImageVector,
    title: @Composable () -> String,
    description: @Composable () -> String?,
    checked: () -> Boolean,
    onClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colors.surface)
            .clickable { onClick() }
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium)
        ) {
            Icon(
                imageVector = icon(),
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface,
            )

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title(),
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.onSurface
                )

                description()?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface,
                    )
                }
            }
            Switch(checked = checked(), onCheckedChange = onCheckedChange)
        }

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

        Divider(modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium))
    }
}