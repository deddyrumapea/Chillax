package com.romnan.chillax.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SoundSerializable(
    val name: String,
    val volume: Float,
)

fun SoundData.toSerializable() = SoundSerializable(
    name = this.name,
    volume = this.volume,
)