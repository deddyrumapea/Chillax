package com.romnan.chillax.data.model

import com.romnan.chillax.domain.model.Category
import com.romnan.chillax.domain.model.UIText

interface CategoryData {
    fun toDomain() = Category(
        name = this.name,
        readableName = this.readableName,
        description = this.description,
        sounds = this.sounds.map { it.toDomain() },
    )

    val name: String
    val readableName: UIText
    val description: UIText
    val sounds: List<SoundData>
}