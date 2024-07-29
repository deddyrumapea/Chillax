package com.romnan.chillax.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PlayerSerializable(
    val sounds: List<SoundSerializable> = emptyList(),
)