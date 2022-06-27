package com.romnan.chillax.data.model

import com.romnan.chillax.domain.model.Sound
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class PlayerStateSerializable(
    val playingSounds: List<Sound>,
) {
    companion object {
        val defaultValue = PlayerStateSerializable(
            playingSounds = persistentListOf(),
        )
    }
}