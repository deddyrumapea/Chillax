package com.romnan.chillax.data.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class CustomMixList(
    val customMixes: List<CustomMixSerializable> = emptyList(),
)