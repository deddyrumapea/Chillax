package com.romnan.chillax.presentation.model

import com.romnan.chillax.domain.model.Sound
import com.romnan.chillax.domain.model.UIText

data class SoundState(
    val name: String,
    val readableName: UIText,
    val iconResId: Int,
    val isSelected: Boolean,
)

fun Sound.toState(
    isSelected: Boolean = false,
) = SoundState(
    name = this.name,
    readableName = this.readableName,
    iconResId = this.iconResId,
    isSelected = isSelected
)