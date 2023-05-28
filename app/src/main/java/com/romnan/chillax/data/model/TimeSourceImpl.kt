package com.romnan.chillax.data.model

import android.os.SystemClock
import com.romnan.chillax.domain.model.TimeSource

class TimeSourceImpl : TimeSource {
    override val elapsedRealTime: Long
        get() = SystemClock.elapsedRealtime()
}