package com.romnan.chillax.core.domain.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.Serializable

data class PlayerState(
    val playingSounds: PersistentList<Sound>
)