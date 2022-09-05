package com.romnan.chillax.presentation.model

import com.romnan.chillax.domain.model.Mood
import com.romnan.chillax.domain.model.UIText

data class MoodPresentation(
    val name: String,
    val readableName: UIText,
    val imageResId: Int,
    val soundsSize: Int,
)

fun Mood.toPresentation() = MoodPresentation(
    name = this.name,
    readableName = this.readableName,
    imageResId = this.imageResId,
    soundsSize = this.sounds.size,
)