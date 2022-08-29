package com.romnan.chillax.presentation.model

import com.romnan.chillax.domain.model.Sound
import com.romnan.chillax.domain.model.UIText

data class SoundPresentation(
    val name: String,
    val readableName: UIText,
    val iconResId: Int,
    val isSelected: Boolean,
    val audioResId: Int,
    val volume: Float,
) {
}

fun Sound.toPresentation(
    isSelected: Boolean = false,
) = SoundPresentation(
    name = this.name,
    readableName = this.readableName,
    iconResId = this.iconResId,
    isSelected = isSelected,
    audioResId = this.audioResId,
    volume = this.volume,
)