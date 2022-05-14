package com.romnan.chillax.core.domain.notification

import androidx.core.app.NotificationCompat
import com.romnan.chillax.core.domain.model.PlayerState

interface NotificationHelper {
    fun getBasePlayerServiceNotification(): NotificationCompat.Builder

    fun updatePlayerServiceNotification(playerState: PlayerState)

    fun removePlayerServiceNotification()
}