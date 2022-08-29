package com.romnan.chillax.presentation.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Tune
import androidx.compose.ui.graphics.vector.ImageVector
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.romnan.chillax.R
import com.romnan.chillax.presentation.composable.destinations.MoodsScreenDestination
import com.romnan.chillax.presentation.composable.destinations.SettingsScreenDestination
import com.romnan.chillax.presentation.composable.destinations.SoundsScreenDestination

enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    Moods(
        direction = MoodsScreenDestination,
        icon = Icons.Default.Apps,
        label = R.string.moods
    ),

    Sounds(
        direction = SoundsScreenDestination,
        icon = Icons.Default.MusicNote,
        label = R.string.sounds
    ),

    Settings(
        direction = SettingsScreenDestination,
        icon = Icons.Default.Tune,
        label = R.string.settings
    )
}