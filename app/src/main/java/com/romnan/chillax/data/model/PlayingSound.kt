package com.romnan.chillax.data.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class PlayingSound(
    val id: String,
    val volume: Float,
    val startedAt: Long,
)