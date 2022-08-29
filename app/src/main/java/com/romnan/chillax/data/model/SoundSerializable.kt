package com.romnan.chillax.data.model

import com.romnan.chillax.data.source.AppDataSource
import com.romnan.chillax.domain.model.Sound
import kotlinx.serialization.Serializable

@Serializable
data class SoundSerializable(
    val name: String,
    val volume: Float,
) {
    fun toDomain(): Sound? {
        val sound = AppDataSource.getSoundFromName(this.name) ?: return null

        return Sound(
            name = this.name,
            readableName = sound.readableName,
            iconResId = sound.iconResId,
            audioResId = sound.audioResId,
            volume = this.volume,
        )
    }
}

