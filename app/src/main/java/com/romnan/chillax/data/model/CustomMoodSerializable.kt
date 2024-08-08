package com.romnan.chillax.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CustomMoodSerializable(
    val uuid: String,
    val readableName: String,
    val imageUri: String,
    val soundIdToVolume: Map<String, Float>,
)