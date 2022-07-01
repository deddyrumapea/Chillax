package com.romnan.chillax.presentation.model

import com.romnan.chillax.domain.model.Category
import com.romnan.chillax.domain.model.Sound
import com.romnan.chillax.domain.model.UIText

data class CategoryState(
    val readableName: UIText,
    val description: UIText,
    val sounds: List<SoundState>,
)

fun Category.toState(
    soundMapper: (Sound) -> SoundState
) = CategoryState(
    readableName = this.readableName,
    description = this.description,
    sounds = this.sounds.map(transform = soundMapper),
)