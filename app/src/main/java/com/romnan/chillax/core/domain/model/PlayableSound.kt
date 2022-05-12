package com.romnan.chillax.core.domain.model

import android.os.Parcelable
import androidx.annotation.RawRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayableSound(
    @RawRes val resId: Int,
    var isPlaying: Boolean = false
) : Parcelable