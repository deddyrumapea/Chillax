package com.romnan.chillax.domain.notification

import androidx.core.app.NotificationCompat
import com.romnan.chillax.domain.model.PlayerState

interface NotificationHelper {
    fun getBasePlayerServiceNotification(): NotificationCompat.Builder

    fun updatePlayerServiceNotification(playerState: PlayerState)

    fun removePlayerServiceNotification()
}