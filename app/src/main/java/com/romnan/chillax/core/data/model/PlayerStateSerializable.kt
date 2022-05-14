package com.romnan.chillax.core.data.model

import com.romnan.chillax.core.domain.model.Sound
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class PlayerStateSerializable(
    val soundsList: List<Sound>,
) {
    companion object {
        val defaultValue = PlayerStateSerializable(
            soundsList = persistentListOf(),
        )
    }
}