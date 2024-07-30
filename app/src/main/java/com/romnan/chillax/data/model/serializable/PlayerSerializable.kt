package com.romnan.chillax.data.model.serializable

import kotlinx.serialization.Serializable

@Serializable
data class PlayerSerializable(
    val sounds: List<SoundSerializable> = emptyList(),
)