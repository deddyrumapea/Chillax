package com.romnan.chillax.data.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class PlayerSerializable(
    val lastPlayedMixId: String? = null,
    val sounds: List<PlayingSound> = emptyList(),
)