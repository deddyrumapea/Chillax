package com.romnan.chillax.presentation.model

import com.romnan.chillax.data.model.SoundData
import com.romnan.chillax.domain.model.UIText

data class SoundPresentation(
    val name: String,
    val readableName: UIText,
    val iconResId: Int,
    val isSelected: Boolean,
)

fun SoundData.toPresentation(
    isSelected: Boolean = false,
) = SoundPresentation(
    name = this.name,
    readableName = this.readableName,
    iconResId = this.iconResId,
    isSelected = isSelected
)