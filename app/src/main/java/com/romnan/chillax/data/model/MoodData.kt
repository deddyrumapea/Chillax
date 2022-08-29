package com.romnan.chillax.data.model

import com.romnan.chillax.domain.model.Mood
import com.romnan.chillax.domain.model.UIText

interface MoodData {
    fun toDomain() = Mood(
        name = this.name,
        readableName = this.readableName,
        imageResId = this.imageResId,
        sounds = this.sounds.map { it.toDomain() },
    )

    val name: String
    val readableName: UIText
    val imageResId: Int
    val sounds: List<SoundData>
}