package com.romnan.chillax.presentation.model

import com.romnan.chillax.domain.model.UIText

data class CategoryPresentation(
    val id: String,
    val readableName: UIText,
    val description: UIText,
    val sounds: List<SoundPresentation>,
)