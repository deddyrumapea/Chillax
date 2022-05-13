package com.romnan.chillax.core.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.romnan.chillax.core.domain.model.Mood

class MoodState(
    @DrawableRes val image: Int,
    @StringRes val name: Int,
    val isPlaying: Boolean
)

fun Mood.toState() = MoodState(
    image = this.image,
    name = this.name,
    isPlaying = false
)