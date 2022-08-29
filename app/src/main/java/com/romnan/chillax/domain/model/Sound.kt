package com.romnan.chillax.domain.model

data class Sound(
    val name: String,
    val readableName: UIText,
    val iconResId: Int,
    val audioResId: Int,
    val volume: Float,
)