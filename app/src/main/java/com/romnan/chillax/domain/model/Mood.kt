package com.romnan.chillax.domain.model

interface Mood {
    val readableName: UIText
    val imageResId: Int
    val sounds: List<Sound>
}