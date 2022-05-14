package com.romnan.chillax.core.data.model

import com.romnan.chillax.core.domain.model.PlayerState
import com.romnan.chillax.core.domain.model.Sound
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.Serializable

@Serializable
data class PlayerStateSerializable(
    val playingSounds: List<Sound>
) {
    fun toPlayerState() = PlayerState(
        playingSounds = this.playingSounds.toPersistentList()
    )

    companion object {
        val defaultValue = PlayerStateSerializable(
            playingSounds = persistentListOf()
        )
    }
}