package com.romnan.chillax.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PlayingSound(
    val id: String,
    val volume: Float,
    val startedAt: Long,
)