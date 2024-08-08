package com.romnan.chillax.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CustomMoodList(
    val customMoods: List<CustomMoodSerializable> = emptyList(),
)