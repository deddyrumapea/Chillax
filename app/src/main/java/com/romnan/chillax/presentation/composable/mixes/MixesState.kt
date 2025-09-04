package com.romnan.chillax.presentation.composable.mixes

import com.romnan.chillax.domain.model.Mix
import com.romnan.chillax.domain.model.Player
import com.romnan.chillax.presentation.composable.mixes.model.MixType

data class MixesState(
    val player: Player? = null,
    val mixes: List<Mix> = emptyList(),
    val customMixToDelete: Mix? = null,
    val mixTypes: List<MixType> = MixType.entries,
    val selectedMixType: MixType = MixType.All,
)