package com.romnan.chillax.presentation.model

import com.romnan.chillax.domain.model.UIText

data class SoundPresentation(
    val id: String,
    val readableName: UIText,
    val iconResId: Int,
    val audioResId: Int,
    val isPlaying: Boolean,
    val volume: Float,
)