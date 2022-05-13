package com.romnan.chillax.core.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.romnan.chillax.core.domain.model.Sound

data class SoundState(
    @DrawableRes val icon: Int,
    @StringRes val name: Int,
    val isPlaying: Boolean
)

fun Sound.toState() = SoundState(
    icon = this.icon,
    name = this.name,
    isPlaying = false
)