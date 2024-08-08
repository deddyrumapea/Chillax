package com.romnan.chillax.domain.model

class Category(
    val id: String,
    val readableName: UIText,
    val description: UIText,
    val soundIds: List<String>,
)