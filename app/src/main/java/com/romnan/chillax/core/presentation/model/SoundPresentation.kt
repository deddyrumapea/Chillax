package com.romnan.chillax.core.presentation.model

import androidx.annotation.RawRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.romnan.chillax.core.domain.model.PlayableSound
import com.romnan.chillax.core.domain.model.Sound
import com.romnan.chillax.core.domain.model.UIText

data class SoundPresentation(
    val icon: ImageVector,
    val name: UIText,
    @RawRes val resId: Int,
    var isPlaying: Boolean = false
) {
    constructor(sound: Sound) : this(
        icon = sound.icon,
        name = sound.name,
        resId = sound.resource
    )

    fun toPlayableSound() = PlayableSound(
        resId = resId,
        isPlaying = isPlaying
    )
}