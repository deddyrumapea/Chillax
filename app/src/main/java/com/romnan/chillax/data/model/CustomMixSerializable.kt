package com.romnan.chillax.data.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class CustomMixSerializable(
    val uuid: String,
    val readableName: String,
    val imageUri: String,
    val soundIdToVolume: Map<String, Float>,
)