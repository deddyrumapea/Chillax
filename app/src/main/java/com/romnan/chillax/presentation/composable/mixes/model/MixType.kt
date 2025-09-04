package com.romnan.chillax.presentation.composable.mixes.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.LibraryAddCheck
import androidx.compose.material.icons.rounded.LibraryMusic
import androidx.compose.ui.graphics.vector.ImageVector
import com.romnan.chillax.R
import com.romnan.chillax.domain.model.UIText

enum class MixType(
    val label: UIText,
    val icon: ImageVector,
) {
    All(
        label = UIText.StringResource(R.string.mix_type_all),
        icon = Icons.Rounded.Apps,
    ),

    Preset(
        label = UIText.StringResource(R.string.mix_type_preset),
        icon = Icons.Rounded.LibraryAddCheck,
    ),

    Custom(
        label = UIText.StringResource(R.string.mix_type_custom),
        icon = Icons.Rounded.LibraryMusic,
    ),
}