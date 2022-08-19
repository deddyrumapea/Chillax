package com.romnan.chillax.domain.model

interface Category {
    val readableName: UIText
    val description: UIText
    val sounds: List<Sound>
}