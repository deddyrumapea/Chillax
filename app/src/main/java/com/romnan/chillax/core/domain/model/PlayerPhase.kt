package com.romnan.chillax.core.domain.model

import androidx.annotation.StringRes
import com.romnan.chillax.R

enum class PlayerPhase(@StringRes val readableName: Int) {
    Playing(readableName = R.string.playing),
    Paused(readableName = R.string.paused),
    Stopped(readableName = R.string.stopped)
}