package com.romnan.chillax.domain.model

data class Mood(
    val name: String,
    val readableName: UIText,
    val imageResId: Int,
    val sounds: List<Sound>,
)