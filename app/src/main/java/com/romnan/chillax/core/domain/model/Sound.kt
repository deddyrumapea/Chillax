package com.romnan.chillax.core.domain.model

import androidx.annotation.RawRes
import androidx.compose.ui.graphics.vector.ImageVector

data class Sound(
    val icon: ImageVector,
    val name: UIText,
    @RawRes val resource: Int,
)