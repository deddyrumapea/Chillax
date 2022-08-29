package com.romnan.chillax.presentation.model

import com.romnan.chillax.data.model.CategoryData
import com.romnan.chillax.data.model.SoundData
import com.romnan.chillax.domain.model.UIText

data class CategoryPresentation(
    val readableName: UIText,
    val description: UIText,
    val sounds: List<SoundPresentation>,
)

fun CategoryData.toPresentation(
    soundMapper: (SoundData) -> SoundPresentation
) = CategoryPresentation(
    readableName = this.readableName,
    description = this.description,
    sounds = this.sounds.map(transform = soundMapper),
)