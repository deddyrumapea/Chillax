package com.romnan.chillax.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PlayerSerializable(
    val lastPlayedMoodId: String? = null,
    val sounds: List<PlayingSound> = emptyList(),
)