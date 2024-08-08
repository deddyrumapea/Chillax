package com.romnan.chillax.domain.model

data class Sound(
    val id: String,
    val readableName: UIText,
    val iconResId: Int,
    val audioResId: Int,
)