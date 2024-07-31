package com.romnan.chillax.domain.model

import androidx.annotation.RawRes

data class Mood(
    val id: String,
    val readableName: UIText,
    @RawRes val imageResId: Int,
    val soundIds: List<String>,
)