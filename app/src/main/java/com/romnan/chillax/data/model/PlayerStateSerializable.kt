package com.romnan.chillax.data.model

import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class PlayerStateSerializable(
    val playingSoundsNames: List<String>,
) {
    companion object {
        val defaultValue = PlayerStateSerializable(
            playingSoundsNames = persistentListOf(),
        )
    }
}