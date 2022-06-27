package com.romnan.chillax.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.romnan.chillax.domain.model.Sound

data class SoundState(
    val id: Int,
    @DrawableRes val icon: Int,
    @StringRes val name: Int,
    @RawRes val resource: Int,
    val isSelected: Boolean
) {
    fun toSound() = Sound(
        id = this.id,
        icon = this.icon,
        name = this.name,
        resource = this.resource
    )
}

fun Sound.toState() = SoundState(
    id = this.id,
    icon = this.icon,
    name = this.name,
    resource = this.resource,
    isSelected = false
)