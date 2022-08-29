package com.romnan.chillax.presentation.model

import com.romnan.chillax.domain.model.Mood
import com.romnan.chillax.domain.model.UIText

data class MoodPresentation(
    val name: String,
    val readableName: UIText,
    val imageResId: Int,
    val sounds: List<SoundPresentation>,
)

fun Mood.toPresentation() = MoodPresentation(
    name = this.name,
    readableName = this.readableName,
    imageResId = this.imageResId,
    sounds = this.sounds.map { it.toPresentation(isSelected = false) }
)