package com.romnan.chillax.presentation.model

import com.romnan.chillax.domain.model.Category
import com.romnan.chillax.domain.model.Sound
import com.romnan.chillax.domain.model.UIText

data class CategoryPresentation(
    val readableName: UIText,
    val description: UIText,
    val sounds: List<SoundPresentation>,
)

fun Category.toPresentation(
    soundToPresentation: (Sound) -> SoundPresentation,
) = CategoryPresentation(
    readableName = this.readableName,
    description = this.description,
    sounds = this.sounds.map(soundToPresentation),
)