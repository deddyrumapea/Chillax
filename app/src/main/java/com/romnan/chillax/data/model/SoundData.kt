package com.romnan.chillax.data.model

import com.romnan.chillax.domain.model.Sound
import com.romnan.chillax.domain.model.UIText

interface SoundData {
    fun toDomain() = Sound(
        name = this.name,
        readableName = this.readableName,
        iconResId = this.iconResId,
        volume = this.volume,
        audioResId = this.audioResId,
    )

    fun toSerializable(
        startedAt: Long
    ): SoundSerializable = SoundSerializable(
        name = this.name,
        volume = this.volume,
        startedAt = startedAt,
    )

    val name: String
    val readableName: UIText
    val iconResId: Int
    val audioResId: Int
    val volume: Float
}