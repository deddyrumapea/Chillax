package com.romnan.chillax.core.presentation.model

import androidx.annotation.RawRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.romnan.chillax.core.domain.model.Sound
import com.romnan.chillax.core.domain.model.UIText

data class PlayableSound(
    val icon: ImageVector,
    val name: UIText,
    @RawRes val resource: Int,
    var isPlaying: Boolean = false
) {
    constructor(sound: Sound) : this(
        icon = sound.icon,
        name = sound.name,
        resource = sound.resource
    )

    fun toSound() = Sound(
        icon = icon,
        name = name,
        resource = resource
    )
}