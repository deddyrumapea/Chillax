package com.romnan.chillax.domain.model

class Category(
    val name: String,
    val readableName: UIText,
    val description: UIText,
    val sounds: List<Sound>,
)