package com.romnan.chillax.data.model

import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class PlayerSerializable(
    val sounds: List<SoundSerializable>
) {
    companion object {
        val defaultValue = PlayerSerializable(
            sounds = persistentListOf()
        )
    }
}